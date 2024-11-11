package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemStorageInMemory implements ItemStorage {
    private final Map<Integer, Item> items;
    private static Integer id = 0;

    @Override
    public Collection<Item> getAllOwnersItems(Integer userId) {
        return items.values().stream()
                .filter(i -> Objects.equals(i.getHostId(), userId))
                .collect(Collectors.toSet());
    }

    @Override
    public Item getItem(Integer itemId) {
        return items.get(itemId);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return items.values().stream()
                .filter(i -> (i.getName().toLowerCase().contains(text.toLowerCase()) ||
                        i.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                        i.getAvailable())
                .collect(Collectors.toSet());
    }

    @Override
    public void createItem(Item item) {
        item.setId(getNewId());
        items.put(item.getId(), item);
    }

    @Override
    public void updateItem(Item item) {
        items.put(item.getId(), item);
    }

    @Override
    public Integer getHost(Integer item) {
        return items.get(item).getHostId();
    }

    private static Integer getNewId() {
        return ++id;
    }
}
