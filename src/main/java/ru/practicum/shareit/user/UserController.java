package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.CorruptedDataException;
import ru.practicum.shareit.user.exception.DuplicateDataException;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Integer userId) {
        return service.getUser(userId);
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto user) throws DuplicateDataException, CorruptedDataException {
        return service.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody @Valid UserDto user, @PathVariable Integer userId)
            throws DuplicateDataException {
        return service.updateUser(user, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        service.deleteUser(userId);
    }
}
