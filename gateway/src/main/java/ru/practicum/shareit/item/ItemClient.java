package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

public class ItemClient extends BaseClient {
    public ItemClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> getAllOwnersItems(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItem(Integer itemId, Integer userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> searchItems(String text, Integer userId) {
        return get("/search?text={text}", Long.valueOf(userId), Map.of("text", text));
    }

    public ResponseEntity<Object> createItem(ItemDto item, Integer userId) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> createComment(CommentDto commentDto, Integer itemId, Integer userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> updateItem(ItemDto item, Integer itemId, Integer userId) {
        return patch("/" + itemId, userId, item);
    }
}
