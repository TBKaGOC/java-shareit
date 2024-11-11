package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.InvalidHostException;
import ru.practicum.shareit.item.exception.NotFoundException;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> getAllOwnersItems(Integer userId);

    ItemDto getItem(Integer itemId);

    Collection<ItemDto> searchItems(String text);

    ItemDto createItem(ItemDto item, Integer userId) throws NotFoundException;

    ItemDto updateItem(ItemDto item, Integer itemId, Integer userId) throws InvalidHostException;
}
