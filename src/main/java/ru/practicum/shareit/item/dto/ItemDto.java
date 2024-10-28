package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Getter
public class ItemDto {
    private Integer id;
    @NotBlank private String name;
    @NotBlank private String description;
    @NotNull private Boolean available;
}
