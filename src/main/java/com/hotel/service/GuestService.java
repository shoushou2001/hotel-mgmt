package com.hotel.service;

import com.hotel.model.Guest;
import com.hotel.repository.GuestRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public List<Guest> findAll() {
        return guestRepository.findAll();
    }

    public Optional<Guest> findById(Long id) {
        return guestRepository.findById(id);
    }

    public List<Guest> search(String keyword) {
        return guestRepository.findByFirstNameContainingOrLastNameContaining(keyword, keyword);
    }

    public Guest save(Guest guest) {
        return guestRepository.save(guest);
    }

    public void delete(Long id) {
        guestRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return guestRepository.existsByEmail(email);
    }
}
