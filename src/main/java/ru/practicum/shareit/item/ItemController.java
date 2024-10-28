package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.InvalidHostException;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @GetMapping
    public Collection<ItemDto> getAllOwnersItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return service.getAllOwnersItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Integer itemId) {
        return service.getItem(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        return service.searchItems(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestBody @Valid ItemDto item, @RequestHeader("X-Sharer-User-Id") Integer userId)
            throws NotFoundException {
        return service.createItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto item,
                              @PathVariable Integer itemId,
                              @RequestHeader("X-Sharer-User-Id") Integer userId)
            throws InvalidHostException {
        return service.updateItem(item, itemId, userId);
    }
}
