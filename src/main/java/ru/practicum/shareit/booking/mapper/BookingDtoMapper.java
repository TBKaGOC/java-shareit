package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingDtoMapper {
    public Booking mapToBooking(BookingDto dto) {
        return Booking.builder()
                .id(dto.getId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .status(dto.getStatus())
                .build();
    }

    public BookingDto mapToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .booker(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }
}
