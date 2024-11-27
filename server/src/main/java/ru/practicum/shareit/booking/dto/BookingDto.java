package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Builder
@Getter
public class BookingDto {
    Integer id;
    @Future
    LocalDateTime start;
    @Future
    LocalDateTime end;
    Integer itemId;
    Integer booker;
    Status status;
}
