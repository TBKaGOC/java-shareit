package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.NotFoundException;
import ru.practicum.shareit.request.mapper.RequestDtoMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository storage;
    private final RequestDtoMapper mapper;

    public ItemRequestDto addRequest(ItemRequestDto requestDto, Integer hostId) {
        requestDto.setHostId(hostId);
        requestDto.setCreated(LocalDateTime.now());

        return mapper.mapToDto(storage.save(mapper.mapToRequest(requestDto)));
    }

    public Collection<ItemRequestDto> getRequests(Integer hostId) {
        return storage.findByHostIdOrderByCreatedDesc(hostId).stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toSet());
    }

    public Collection<ItemRequestDto> getAll() {
        return storage.findAllByOrderByCreatedDesc().stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toSet());
    }

    public ItemRequestDto getOne(Integer id) throws NotFoundException {
        return mapper.mapToDto(storage.findById(id).orElseThrow(
                () -> new NotFoundException("Не найден запрос " + id)
        ));
    }
}
