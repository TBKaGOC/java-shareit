package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Builder
@Getter
public class BookingReturnDto {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    User booker;
    Status status;
}
