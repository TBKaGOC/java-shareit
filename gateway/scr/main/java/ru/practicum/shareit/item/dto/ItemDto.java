package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ItemDto {
    @NotBlank
    private String name;
    @NotBlank private String description;
    @NotNull
    private Boolean available;
    private Integer requestId;
}
