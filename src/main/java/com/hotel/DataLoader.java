package com.hotel;

import com.hotel.model.*;
import com.hotel.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;

    public DataLoader(RoomRepository roomRepository, GuestRepository guestRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) {
        Room r1 = roomRepository.save(new Room("101", "SINGLE", 1, new BigDecimal("89.00"), "AVAILABLE"));
        Room r2 = roomRepository.save(new Room("102", "SINGLE", 1, new BigDecimal("89.00"), "AVAILABLE"));
        Room r3 = roomRepository.save(new Room("201", "DOUBLE", 2, new BigDecimal("129.00"), "AVAILABLE"));
        Room r4 = roomRepository.save(new Room("202", "DOUBLE", 2, new BigDecimal("129.00"), "OCCUPIED"));
        Room r5 = roomRepository.save(new Room("301", "SUITE", 3, new BigDecimal("199.00"), "AVAILABLE"));
        Room r6 = roomRepository.save(new Room("401", "DELUXE", 4, new BigDecimal("299.00"), "MAINTENANCE"));

        Guest g1 = guestRepository.save(new Guest("Alice", "Johnson", "alice@email.com", "555-0101"));
        Guest g2 = guestRepository.save(new Guest("Bob", "Smith", "bob@email.com", "555-0102"));
        Guest g3 = guestRepository.save(new Guest("Carol", "Williams", "carol@email.com", "555-0103"));

        Booking b1 = new Booking(r4, g1, LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), "CHECKED_IN");
        b1.setNotes("Business trip");
        bookingRepository.save(b1);

        Booking b2 = new Booking(r1, g2, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), "CONFIRMED");
        b2.setNotes("Weekend getaway");
        bookingRepository.save(b2);

        System.out.println("=== Sample data loaded ===");
    }
}
