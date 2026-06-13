package com.hotel.controller;

import com.hotel.model.Room;
import com.hotel.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "room/list";
    }

    @GetMapping("/new")
    public String addForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("types", new String[]{"SINGLE", "DOUBLE", "SUITE", "DELUXE"});
        return "room/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return roomService.findById(id).map(room -> {
            model.addAttribute("room", room);
            model.addAttribute("types", new String[]{"SINGLE", "DOUBLE", "SUITE", "DELUXE"});
            return "room/form";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("error", "Room not found");
            return "redirect:/rooms";
        });
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Room room, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("types", new String[]{"SINGLE", "DOUBLE", "SUITE", "DELUXE"});
            return "room/form";
        }
        if (room.getId() == null && roomService.existsByRoomNumber(room.getRoomNumber())) {
            model.addAttribute("types", new String[]{"SINGLE", "DOUBLE", "SUITE", "DELUXE"});
            model.addAttribute("dupError", "Room number already exists");
            return "room/form";
        }
        roomService.save(room);
        redirect.addFlashAttribute("success", "Room saved successfully");
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            roomService.delete(id);
            redirect.addFlashAttribute("success", "Room deleted");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Cannot delete: room has active bookings");
        }
        return "redirect:/rooms";
    }
}
