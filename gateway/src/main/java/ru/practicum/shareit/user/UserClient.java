package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

public class UserClient extends BaseClient {
    public UserClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> getUser(Integer userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> createUser(UserDto user) {
        return post("", user);
    }

    public ResponseEntity<Object> updateUser(UserDto user, Integer userId) {
        return patch("/" + userId, user);
    }

    public ResponseEntity<Object> deleteUser(Integer userId) {
        return delete("/" + userId, userId);
    }
}
