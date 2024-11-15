package ru.practicum.shareit.user.service;

import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.CorruptedDataException;
import ru.practicum.shareit.user.exception.DuplicateDataException;

public interface UserService {
    UserDto getUser(Integer userId) throws NotFoundException;

    UserDto createUser(UserDto user) throws DuplicateDataException, CorruptedDataException, NotFoundException;

    UserDto updateUser(UserDto user, Integer userId) throws DuplicateDataException, NotFoundException;

    void deleteUser(Integer userId);
}
