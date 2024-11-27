package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;

public class RequestDto {
    @NotNull
    String description;
}
