package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentDto {
    @NotBlank
    private String text;
}
