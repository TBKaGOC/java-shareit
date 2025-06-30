package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentReturnDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.exception.InvalidBookingException;
import ru.practicum.shareit.item.exception.InvalidHostException;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
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
    public Collection<ItemDtoWithDate> getAllOwnersItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return service.getAllOwnersItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithDate getItem(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId)
            throws NotFoundException {
        return service.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        return service.searchItems(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") Integer userId)
            throws NotFoundException {
        return service.createItem(item, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentReturnDto createComment(@RequestBody Comment comment,
                                          @PathVariable Integer itemId,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId) throws InvalidBookingException {
        return service.createComment(comment, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto item,
                              @PathVariable Integer itemId,
                              @RequestHeader("X-Sharer-User-Id") Integer userId)
            throws NotFoundException, InvalidHostException {
        return service.updateItem(item, itemId, userId);
    }
}
