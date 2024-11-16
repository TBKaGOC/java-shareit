package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ItemDateDtoMapper {
    private final ItemStorage storage;

    public Item mapToItem(ItemDtoWithDate dto) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .comments(dto.getComments())
                .build();
    }

    public ItemDtoWithDate mapToDto(Item item) {
        LocalDateTime now = LocalDateTime.now();

        return ItemDtoWithDate.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(item.getComments())
                .lastBooking(storage.findLastBooking(item.getId(), now))
                .nextBooking(storage.findNextBooking(item.getId(), now))
                .build();
    }
}
