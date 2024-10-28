package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Collection<Item> getAllOwnersItems(Integer userId);

    Item getItem(Integer itemId);

    Collection<Item> searchItems(String text);

    void createItem(Item item);

    void updateItem(Item item);

    Integer getHost(Integer item);
}
