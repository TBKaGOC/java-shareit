package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.dao.BookingStorage;
import ru.practicum.shareit.booking.exception.UnavailableItemException;
import ru.practicum.shareit.item.dao.CommentStorage;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.CommentReturnDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.exception.InvalidBookingException;
import ru.practicum.shareit.item.exception.InvalidHostException;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemDateDtoMapper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDtoMapper mapper;
    private final ItemDateDtoMapper dateMapper;
    private final ItemStorage storage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;

    @Override
    public Collection<ItemDtoWithDate> getAllOwnersItems(Integer userId) {
        return storage.findByHostId(userId).stream()
                .map(dateMapper::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public ItemDtoWithDate getItem(Integer itemId, Integer userId) throws NotFoundException {
        return dateMapper.mapToDto(findItemOrThrow(itemId));
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (!StringUtils.hasText(text)) {
            return new ArrayList<>();
        }
        return storage.findAvailableByQuery(text)
                .stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public ItemDto createItem(ItemDto item, Integer userId) throws NotFoundException {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Пользователь " + userId + " не существует");
        }

        Item res = mapper.mapToItem(item);
        res.setHostId(userId);

        Item result = storage.save(res);
        return mapper.mapToDto(result);
    }

    @Override
    public CommentReturnDto createComment(Comment comment, Integer itemId, Integer userId) throws InvalidBookingException {
        if (!bookingStorage.containsUserBooking(itemId, userId, LocalDateTime.now())) {
            throw new InvalidBookingException("Пользователь " + userId + " не арендовал предмет " + itemId);
        }

        comment.setItemId(itemId);
        comment.setUserId(userId);

        comment = commentStorage.save(comment);

        return CommentReturnDto.builder()
                .id(comment.getId())
                .itemId(comment.getItemId())
                .authorName(userStorage.getName(userId))
                .created(LocalDate.now())
                .text(comment.getText()).build();
    }

    @Override
    public ItemDto updateItem(ItemDto item, Integer itemId, Integer userId) throws InvalidHostException,
            NotFoundException {
        if (!Objects.equals(storage.findUserIdById(itemId), userId)) {
            throw new InvalidHostException("Пользователь " + userId +
                    " не является владельцем предмета " + item.getDescription());
        }

        Item old = findItemOrThrow(itemId);
        Item newItem = Item.builder()
                .id(itemId)
                .name(Optional.ofNullable(item.getName()).orElse(old.getName()))
                .description(Optional.ofNullable(item.getDescription()).orElse(old.getDescription()))
                .available(Optional.ofNullable(item.getAvailable()).orElse(old.getAvailable()))
                .build();

        return mapper.mapToDto(storage.save(newItem));
    }

    @Override
    public Item findItemOrThrow(Integer itemId) throws NotFoundException {
        return storage.findById(itemId).orElseThrow(
                () -> new NotFoundException("Предмет " + itemId + " не найден")
        );
    }

    @Override
    public void throwNotAvailable(Integer itemId) throws UnavailableItemException {
        if (!storage.findAvailableById(itemId)) {
            throw new UnavailableItemException("Предмет " + itemId + " не доступен");
        }
    }

    @Override
    public void throwNotFound(Integer itemId) throws ru.practicum.shareit.booking.exception.NotFoundException {
        if (!storage.existsById(itemId)) {
            throw new ru.practicum.shareit.booking.exception.NotFoundException("Предмет " + itemId + " не существует");
        }
    }

    @Override
    public Integer findHost(Integer itemId) {
        return storage.findUserIdById(itemId);
    }
}
