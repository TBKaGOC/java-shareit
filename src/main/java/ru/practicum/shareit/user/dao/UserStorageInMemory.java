package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserStorageInMemory implements UserStorage {
    private final Map<Integer, User> users;
    private static Integer id = 0;

    @Override
    public User getUser(Integer userId) {
        return users.get(userId);
    }

    @Override
    public void createUser(User user) {
        user.setId(getNewId());
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(Integer userId) {
        users.remove(userId);
    }

    @Override
    public boolean containsUser(Integer userId) {
        return users.containsKey(userId);
    }

    @Override
    public boolean containsEmail(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private static Integer getNewId() {
        return ++id;
    }
}
