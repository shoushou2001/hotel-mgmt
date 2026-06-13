package com.hotel.repository;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByStatus(String status);

    @Query("SELECT b FROM Booking b JOIN FETCH b.room JOIN FETCH b.guest ORDER BY b.checkIn DESC")
    List<Booking> findAllWithDetails();

    @Query("SELECT b FROM Booking b JOIN FETCH b.room JOIN FETCH b.guest WHERE b.id = :id")
    Booking findByIdWithDetails(Long id);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b " +
           "WHERE b.room = :room AND b.status IN ('CONFIRMED', 'CHECKED_IN') " +
           "AND b.checkIn < :checkOut AND b.checkOut > :checkIn")
    boolean isRoomOccupied(Room room, LocalDate checkIn, LocalDate checkOut);
}
