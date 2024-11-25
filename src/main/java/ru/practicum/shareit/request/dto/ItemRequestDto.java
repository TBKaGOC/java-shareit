package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@Getter
public class ItemRequestDto {
    Integer id;
    @NotNull
    String description;
    @Setter
    Integer hostId;
    @Setter
    LocalDateTime created;
    Set<Item> items;
}
