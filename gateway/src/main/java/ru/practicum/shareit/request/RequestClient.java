package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestDto;

public class RequestClient extends BaseClient {
    public RequestClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> getRequests(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAll() {
        return get("/all");
    }

    public ResponseEntity<Object> getOne(Integer requestId) {
        return get("/" + requestId);
    }

    public ResponseEntity<Object> addRequest(RequestDto request, Integer userId) {
        return post("", userId, request);
    }
}
