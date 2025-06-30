package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient client;

    @GetMapping
    public ResponseEntity<Object> getAllOwnersItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.getAllOwnersItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Integer itemId,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                           @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.searchItems(text, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody @Valid ItemDto item,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.createItem(item, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestBody CommentDto comment,
                                          @PathVariable Integer itemId,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.createComment(comment, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto item,
                              @PathVariable Integer itemId,
                              @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.updateItem(item, itemId, userId);
    }
}
