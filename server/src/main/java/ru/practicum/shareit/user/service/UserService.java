package ru.practicum.shareit.user.service;

import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.DuplicateDataException;

public interface UserService {
    UserDto getUser(Integer userId) throws NotFoundException;

    UserDto createUser(UserDto user) throws DuplicateDataException, NotFoundException;

    UserDto updateUser(UserDto user, Integer userId) throws DuplicateDataException, NotFoundException;

    void deleteUser(Integer userId);

    void throwNotFound(Integer userId) throws ru.practicum.shareit.booking.exception.NotFoundException;

    User findUserOrThrow(Integer userId) throws NotFoundException;
}
