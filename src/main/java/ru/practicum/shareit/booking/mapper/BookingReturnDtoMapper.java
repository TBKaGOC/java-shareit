package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingReturnDtoMapper {
    public Booking mapToBooking(BookingReturnDto dto) {
        return Booking.builder()
                .id(dto.getId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(dto.getItem())
                .booker(dto.getBooker())
                .status(dto.getStatus())
                .build();
    }

    public BookingReturnDto mapToDto(Booking booking) {
        return BookingReturnDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }
}
