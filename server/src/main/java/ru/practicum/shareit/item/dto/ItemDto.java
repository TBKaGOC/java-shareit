package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.item.model.Comment;

import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Getter
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Set<Comment> comments;
    private Integer requestId;
}
