package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.CorruptedDataException;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient client;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Integer userId) {
        return client.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto user) throws CorruptedDataException {
        if (user.getEmail() == null) {
            throw new CorruptedDataException("Емэйл не может быть null");
        }
        return client.createUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserDto user, @PathVariable Integer userId) {
        return client.updateUser(user, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        client.deleteUser(userId);
    }
}
