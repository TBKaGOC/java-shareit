package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
public class ItemDtoWithDate {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank private String description;
    @NotNull
    private Boolean available;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Set<Comment> comments;
}
