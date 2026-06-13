package com.hotel.controller;

import com.hotel.repository.BookingRepository;
import com.hotel.repository.GuestRepository;
import com.hotel.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;

    public HomeController(RoomRepository roomRepository, GuestRepository guestRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalRooms", roomRepository.count());
        model.addAttribute("availableRooms", roomRepository.findByStatus("AVAILABLE").size());
        model.addAttribute("totalGuests", guestRepository.count());
        model.addAttribute("activeBookings", bookingRepository.findByStatus("CHECKED_IN").size());
        return "home";
    }
}
