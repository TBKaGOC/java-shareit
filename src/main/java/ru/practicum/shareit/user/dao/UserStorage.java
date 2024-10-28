package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

public interface UserStorage {
    User getUser(Integer userId);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(Integer userId);

    boolean containsUser(Integer userId);

    boolean containsEmail(String email);
}
