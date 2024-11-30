package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class RequestDtoMapper {
    public ItemRequestDto mapToDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .hostId(request.getHostId())
                .created(request.getCreated())
                .items(request.getItems())
                .build();
    }

    public ItemRequest mapToRequest(ItemRequestDto request) {
        return ItemRequest.builder()
                .id(request.getId())
                .description(request.getDescription())
                .hostId(request.getHostId())
                .created(request.getCreated())
                .items(request.getItems())
                .build();
    }
}
