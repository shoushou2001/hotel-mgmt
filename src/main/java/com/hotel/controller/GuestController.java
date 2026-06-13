package com.hotel.controller;

import com.hotel.model.Guest;
import com.hotel.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("guests", guestService.search(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("guests", guestService.findAll());
        }
        return "guest/list";
    }

    @GetMapping("/new")
    public String addForm(Model model) {
        model.addAttribute("guest", new Guest());
        return "guest/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return guestService.findById(id).map(guest -> {
            model.addAttribute("guest", guest);
            return "guest/form";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("error", "Guest not found");
            return "redirect:/guests";
        });
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Guest guest, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) return "guest/form";
        if (guest.getId() == null && guestService.existsByEmail(guest.getEmail())) {
            redirect.addFlashAttribute("dupError", "Email already registered");
            redirect.addFlashAttribute("guest", guest);
            return "redirect:/guests/new";
        }
        guestService.save(guest);
        redirect.addFlashAttribute("success", "Guest saved successfully");
        return "redirect:/guests";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            guestService.delete(id);
            redirect.addFlashAttribute("success", "Guest deleted");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Cannot delete guest with existing bookings");
        }
        return "redirect:/guests";
    }
}
