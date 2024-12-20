package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.exception.UnavailableItemException;
import ru.practicum.shareit.item.dto.CommentReturnDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.exception.InvalidBookingException;
import ru.practicum.shareit.item.exception.InvalidHostException;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDtoWithDate> getAllOwnersItems(Integer userId);

    ItemDtoWithDate getItem(Integer itemId, Integer userId) throws NotFoundException;

    Collection<ItemDto> searchItems(String text);

    ItemDto createItem(ItemDto item, Integer userId) throws NotFoundException;

    CommentReturnDto createComment(Comment comment, Integer itemId, Integer userId) throws InvalidBookingException;

    ItemDto updateItem(ItemDto item, Integer itemId, Integer userId) throws InvalidHostException, NotFoundException;

    Item findItemOrThrow(Integer itemId) throws NotFoundException;

    void throwNotAvailable(Integer itemId) throws UnavailableItemException;

    void throwNotFound(Integer itemId) throws ru.practicum.shareit.booking.exception.NotFoundException;

    Integer findHost(Integer itemId);
}
