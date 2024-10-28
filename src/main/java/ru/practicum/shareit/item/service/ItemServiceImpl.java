package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.InvalidHostException;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDtoMapper mapper;
    private final ItemStorage storage;
    private final UserStorage userStorage;

    @Override
    public Collection<ItemDto> getAllOwnersItems(Integer userId) {
        return storage.getAllOwnersItems(userId).stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        return mapper.mapToDto(storage.getItem(itemId));
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return storage.searchItems(text).stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public ItemDto createItem(ItemDto item, Integer userId) throws NotFoundException {
        if (!userStorage.containsUser(userId)) {
            throw new NotFoundException("Пользователь " + userId + " не существует");
        }

        Item res = mapper.mapToItem(item);
        res.setHostId(userId);

        storage.createItem(res);
        return mapper.mapToDto(storage.getItem(res.getId()));
    }

    @Override
    public ItemDto updateItem(ItemDto item, Integer itemId, Integer userId) throws InvalidHostException {
        if (!Objects.equals(storage.getHost(itemId), userId)) {
            throw new InvalidHostException("Пользователь " + userId +
                    " не является владельцем предмета " + item.getDescription());
        }

        Item old = storage.getItem(itemId);
        Item newItem = Item.builder()
                .id(itemId)
                .name(item.getName() == null ? old.getName() : item.getName())
                .description(item.getDescription() == null ? old.getDescription() : item.getDescription())
                .available(item.getAvailable() == null ? old.getAvailable() : item.getAvailable())
                .build();

        storage.updateItem(newItem);

        return mapper.mapToDto(newItem);
    }
}
