package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.exception.CorruptedDataException;
import ru.practicum.shareit.booking.exception.InvalidHostException;
import ru.practicum.shareit.booking.exception.NotFoundException;
import ru.practicum.shareit.booking.exception.UnavailableItemException;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @GetMapping("/{bookingId}")
    public BookingReturnDto getBooking(@PathVariable Integer bookingId, @RequestHeader("X-Sharer-User-Id") Integer userId)
            throws InvalidHostException, NotFoundException {
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingReturnDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                        @RequestParam(required = false, defaultValue = "ALL") State state)
            throws NotFoundException {
        return service.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingReturnDto> getHostBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                        @RequestParam(required = false, defaultValue = "ALL") State state)
            throws NotFoundException {
        return service.getHostBookings(userId, state);
    }

    @PostMapping
    public BookingReturnDto addBooking(@RequestBody @Valid BookingDto booking, @RequestHeader("X-Sharer-User-Id") Integer userId)
            throws CorruptedDataException, NotFoundException, UnavailableItemException, ru.practicum.shareit.item.exception.NotFoundException {
        return service.addBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingReturnDto updateBookingStatus(@PathVariable Integer bookingId,
                                          @RequestParam boolean approved,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId)
            throws InvalidHostException, NotFoundException {
        return service.updateBookingStatus(bookingId, approved, userId);
    }
}
