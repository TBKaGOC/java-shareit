package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingStorage extends JpaRepository<Booking, Integer> {
    //Queries for user's bookings
    @Query(value = "SELECT b.* FROM bookings AS b WHERE b.user_id = ?1 ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getUserBooking(Integer userId);

    @Query(value = "SELECT b.* FROM bookings AS b " +
            "WHERE b.user_id = ?1 AND ?2 BETWEEN b.start AND b.booking_end " +
            "ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getUserBookingInProgress(Integer userId, LocalDate now);

    @Query(value = "SELECT b.* FROM bookings AS b WHERE b.user_id = ?1 " +
            "AND b.booking_end >= ?2 " +
            "ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getUserBookingInPast(Integer userId, LocalDate now);

    @Query(value = "SELECT b.* FROM bookings AS b WHERE b.user_id = ?1 " +
            "AND b.start <= ?2 " +
            "ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getUserBookingFuture(Integer userId, LocalDate now);

    @Query(value = "SELECT b.* FROM bookings AS b WHERE b.user_id = ?1 " +
            "AND b.status = ?2 " +
            "ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getUserBookingStatus(Integer userId, String status);

    //Queries for item's bookings
    @Query(value = "SELECT b.* FROM bookings AS b " +
            "LEFT outer JOIN items as i ON i.id = b.item_id " +
            "WHERE i.user_id = ?1 " +
            "ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getHostBooking(Integer userId);

    @Query(value = "SELECT b.* FROM bookings AS b " +
            "LEFT outer JOIN items as i ON i.id = b.item_id " +
            "WHERE i.user_id = ?1 AND ?2 BETWEEN b.start AND b.booking_end " +
            "ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getHostBookingInProgress(Integer userId, LocalDate now);

    @Query(value = "SELECT b.* FROM bookings AS b " +
            "LEFT outer JOIN items as i ON i.id = b.item_id " +
            "WHERE i.user_id = ?1 AND b.booking_end >= ?2 " +
            "ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getHostBookingInPast(Integer userId, LocalDate now);

    @Query(value = "SELECT b.* FROM bookings AS b " +
            "LEFT outer JOIN items as i ON i.id = b.item_id " +
            "WHERE i.user_id = ?1 AND b.start <= ?2 " +
            "ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getHostBookingFuture(Integer userId, LocalDate now);

    @Query(value = "SELECT b.* FROM bookings AS b " +
            "LEFT outer JOIN items as i ON i.id = b.item_id " +
            "WHERE i.user_id = ?1 AND b.status = ?2 " +
            "ORDER BY b.start DESC", nativeQuery = true)
    Collection<Booking> getHostBookingStatus(Integer userId, String status);

    @Query(value = "SELECT EXISTS(SELECT b.* FROM bookings AS b" +
            " WHERE b.item_id = ?1 AND b.user_id = ?2 AND b.start > ?3)", nativeQuery = true)
    boolean containsUserBooking(Integer itemId, Integer userId, LocalDateTime now);
}
