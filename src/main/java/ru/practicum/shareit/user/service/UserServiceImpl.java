package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.CorruptedDataException;
import ru.practicum.shareit.user.exception.DuplicateDataException;
import ru.practicum.shareit.user.mapper.UserDtoMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage storage;
    private final UserDtoMapper mapper;

    @Override
    public UserDto getUser(Integer userId) throws NotFoundException {
        return mapper.mapToDto(storage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь " + userId + " не существует"))
        );
    }

    @Override
    public UserDto createUser(UserDto user) throws DuplicateDataException, CorruptedDataException, NotFoundException {
        if (user.getEmail() == null) {
            throw new CorruptedDataException("Е-мэйл не может быть null");
        }
        if (storage.containsEmail(user.getEmail())) {
            throw new DuplicateDataException("Е-мэйл " + user.getEmail() + " уже используется");
        }

        User res = mapper.mapToUser(user);
        return mapper.mapToDto(storage.save(res));
    }

    @Override
    public UserDto updateUser(UserDto user, Integer userId) throws DuplicateDataException, NotFoundException {
        if (user.getEmail() != null && storage.containsEmail(user.getEmail())) {
            throw new DuplicateDataException("Е-мэйл " + user.getEmail() + " уже используется");
        }
        User old = storage.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь " + user + " не существует")
        );

        User newUser = User.builder()
                .id(userId)
                .email(Optional.ofNullable(user.getEmail()).orElse(old.getEmail()))
                .name(Optional.ofNullable(user.getName()).orElse(old.getName()))
                .build();

        return mapper.mapToDto(storage.save(newUser));
    }

    @Override
    public void deleteUser(Integer userId) {
        storage.deleteById(userId);
    }
}
