package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class UserDto {
    @Setter private Integer id;
    private String name;
    private String email;
}
