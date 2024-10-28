package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.CorruptedDataException;
import ru.practicum.shareit.user.exception.DuplicateDataException;
import ru.practicum.shareit.user.mapper.UserDtoMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage storage;
    private final UserDtoMapper mapper;

    @Override
    public UserDto getUser(Integer userId) {
        return mapper.mapToDto(storage.getUser(userId));
    }

    @Override
    public UserDto createUser(UserDto user) throws DuplicateDataException, CorruptedDataException {
        if (user.getEmail() == null) {
            throw new CorruptedDataException("Е-мэйл не может быть null");
        }
        if (storage.containsEmail(user.getEmail())) {
            throw new DuplicateDataException("Е-мэйл " + user.getEmail() + " уже используется");
        }

        User res = mapper.mapToUser(user);
        storage.createUser(res);
        return mapper.mapToDto(storage.getUser(res.getId()));
    }

    @Override
    public UserDto updateUser(UserDto user, Integer userId) throws DuplicateDataException {
        if (user.getEmail() != null && storage.containsEmail(user.getEmail())) {
            throw new DuplicateDataException("Е-мэйл " + user.getEmail() + " уже используется");
        }
        User old = storage.getUser(userId);

        User newUser = User.builder()
                .id(userId)
                .email(user.getEmail() == null ? old.getEmail() : user.getEmail())
                .name(user.getName() == null ? old.getName() : user.getName())
                .build();
        storage.updateUser(newUser);

        return mapper.mapToDto(storage.getUser(userId));
    }

    @Override
    public void deleteUser(Integer userId) {
        storage.deleteUser(userId);
    }
}
