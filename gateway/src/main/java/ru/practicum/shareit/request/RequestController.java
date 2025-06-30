package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestClient client;

    @GetMapping
    public ResponseEntity<Object> getUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return client.getAll();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> get(@PathVariable Integer requestId) {
        return client.getOne(requestId);
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody @Valid RequestDto request,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return client.addRequest(request, userId);
    }
}
