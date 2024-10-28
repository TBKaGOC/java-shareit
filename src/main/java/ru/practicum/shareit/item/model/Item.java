package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */

@Builder
@Getter
public class Item {
    @Setter private Integer id;
    @NotBlank private String name;
    @NotBlank private String description;
    @Setter private Integer hostId;
    private Boolean available;
}
