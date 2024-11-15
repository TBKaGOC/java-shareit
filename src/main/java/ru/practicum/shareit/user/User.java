package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */

@Builder
@Getter
public class User {
    @Setter private Integer id;
    @NotBlank private String name;
    @Email private String email;
}
