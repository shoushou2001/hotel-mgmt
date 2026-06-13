package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.service.BookingService;
import com.hotel.service.GuestService;
import com.hotel.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final GuestService guestService;

    public BookingController(BookingService bookingService, RoomService roomService, GuestService guestService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("bookings", bookingService.findAll());
        return "booking/list";
    }

    @GetMapping("/new")
    public String addForm(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("rooms", roomService.findAvailable());
        model.addAttribute("guests", guestService.findAll());
        return "booking/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Booking booking,
                       @RequestParam Long roomId,
                       @RequestParam Long guestId,
                       @RequestParam String checkInStr,
                       @RequestParam String checkOutStr,
                       RedirectAttributes redirect) {
        Room room = roomService.findById(roomId).orElse(null);
        Guest guest = guestService.findById(guestId).orElse(null);
        if (room == null || guest == null) {
            redirect.addFlashAttribute("error", "Invalid room or guest");
            return "redirect:/bookings/new";
        }
        LocalDate checkIn = LocalDate.parse(checkInStr);
        LocalDate checkOut = LocalDate.parse(checkOutStr);
        if (!checkOut.isAfter(checkIn)) {
            redirect.addFlashAttribute("error", "Check-out must be after check-in");
            return "redirect:/bookings/new";
        }
        if (!bookingService.isRoomAvailable(room, checkIn, checkOut)) {
            redirect.addFlashAttribute("error", "Room is not available for these dates");
            return "redirect:/bookings/new";
        }
        booking.setRoom(room);
        booking.setGuest(guest);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        bookingService.createBooking(booking);
        redirect.addFlashAttribute("success", "Booking created successfully");
        return "redirect:/bookings";
    }

    @GetMapping("/{id}/checkin")
    public String checkIn(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            bookingService.checkIn(id);
            redirect.addFlashAttribute("success", "Guest checked in");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Check-in failed: " + e.getMessage());
        }
        return "redirect:/bookings";
    }

    @GetMapping("/{id}/checkout")
    public String checkOut(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            bookingService.checkOut(id);
            redirect.addFlashAttribute("success", "Guest checked out");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Check-out failed: " + e.getMessage());
        }
        return "redirect:/bookings";
    }

    @GetMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            bookingService.cancel(id);
            redirect.addFlashAttribute("success", "Booking cancelled");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Cancellation failed: " + e.getMessage());
        }
        return "redirect:/bookings";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            bookingService.delete(id);
            redirect.addFlashAttribute("success", "Booking deleted");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Delete failed: " + e.getMessage());
        }
        return "redirect:/bookings";
    }
}
