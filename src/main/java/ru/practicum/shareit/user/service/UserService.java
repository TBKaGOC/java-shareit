package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.CorruptedDataException;
import ru.practicum.shareit.user.exception.DuplicateDataException;

public interface UserService {
    UserDto getUser(Integer userId);

    UserDto createUser(UserDto user) throws DuplicateDataException, CorruptedDataException;

    UserDto updateUser(UserDto user, Integer userId) throws DuplicateDataException;

    void deleteUser(Integer userId);
}
