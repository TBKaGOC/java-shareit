package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.NotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService service;

    @GetMapping
    public Collection<ItemRequestDto> getUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return service.getRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto get(@PathVariable Integer requestId) throws NotFoundException {
        return service.getOne(requestId);
    }

    @PostMapping
    public ItemRequestDto addRequest(@RequestBody ItemRequestDto request, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return service.addRequest(request, userId);
    }
}
