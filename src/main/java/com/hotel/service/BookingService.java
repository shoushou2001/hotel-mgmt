package com.hotel.service;

import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.GuestRepository;
import com.hotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAllWithDetails();
    }

    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(bookingRepository.findByIdWithDetails(id));
    }

    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return !bookingRepository.isRoomOccupied(room, checkIn, checkOut);
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        booking.setStatus("CONFIRMED");
        Booking saved = bookingRepository.save(booking);
        Room room = booking.getRoom();
        room.setStatus("OCCUPIED");
        roomRepository.save(room);
        return saved;
    }

    @Transactional
    public void checkIn(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CHECKED_IN");
        bookingRepository.save(booking);
    }

    @Transactional
    public void checkOut(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CHECKED_OUT");
        booking.getRoom().setStatus("AVAILABLE");
        roomRepository.save(booking.getRoom());
        bookingRepository.save(booking);
    }

    @Transactional
    public void cancel(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CANCELLED");
        booking.getRoom().setStatus("AVAILABLE");
        roomRepository.save(booking.getRoom());
        bookingRepository.save(booking);
    }

    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
