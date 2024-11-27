package ru.practicum.shareit.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.NotFoundException;
import ru.practicum.shareit.request.handler.RequestErrorHandler;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTests {
    @Mock
    private ItemRequestService service;
    @InjectMocks
    private ItemRequestController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemRequestDto dto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(RequestErrorHandler.class)
                .build();

        dto = ItemRequestDto.builder()
                .id(1)
                .description("d")
                .hostId(1)
                .build();
    }

    @Test
    void testSave() throws Exception {
         when(service.addRequest(any(), any()))
                 .thenReturn(dto);

         mvc.perform(post("/requests")
                 .content(mapper.writeValueAsString(dto))
                 .characterEncoding(StandardCharsets.UTF_8)
                 .contentType(MediaType.APPLICATION_JSON)
                 .accept(MediaType.APPLICATION_JSON)
                 .header("X-Sharer-User-Id", 1))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.id", is(dto.getId()), Integer.class))
                 .andExpect(jsonPath("$.description", is(dto.getDescription())))
                 .andExpect(jsonPath("$.hostId", is(dto.getHostId()), Integer.class));
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAll())
                .thenReturn(List.of(dto));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOne() throws Exception {
        when(service.getOne(any()))
                .thenReturn(dto);

        mvc.perform(get("/requests/" + dto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.hostId", is(dto.getHostId()), Integer.class));
    }

    @Test
    void testGetUser() throws Exception {
        when(service.getRequests(any()))
                .thenReturn(List.of(dto));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOneGetNotFound() throws Exception {
        when(service.getOne(any()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/requests/" + dto.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    void testSaveGetValidationException() throws Exception {
        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(ItemRequestDto.builder().build()))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400));
    }

    @Test
    void testSaveGetException() throws Exception {
        when(service.addRequest(any(), any()))
                .thenThrow(RuntimeException.class);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(500));
    }
}
