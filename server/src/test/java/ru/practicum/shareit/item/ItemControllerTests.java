package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.CommentReturnDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.exception.InvalidBookingException;
import ru.practicum.shareit.item.exception.InvalidHostException;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.item.handler.ItemErrorHandler;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTests {
    @Mock
    private ItemService service;
    @InjectMocks
    private ItemController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemDto dto;
    private ItemDtoWithDate dtoWithDate;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ItemErrorHandler.class)
                .build();

        dto = ItemDto.builder()
                .id(1)
                .name("name")
                .available(true)
                .description("description")
                .build();

        dtoWithDate = ItemDtoWithDate.builder()
                .id(1)
                .name("name")
                .description("description")
                .build();
    }

    @Test
    void testSaveItem() throws Exception {
        when(service.createItem(any(), any()))
                .thenReturn(dto);

        mvc.perform(post("/items")
                .content(mapper.writeValueAsString(dto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())));
    }

    @Test
    void testGetItem() throws Exception {
        when(service.getItem(any(), any()))
                .thenReturn(dtoWithDate);

        mvc.perform(get("/items/" + dtoWithDate.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dtoWithDate.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(dtoWithDate.getName())))
                .andExpect(jsonPath("$.description", is(dtoWithDate.getDescription())));
    }

    @Test
    void testGetUserItems() throws Exception {
        when(service.getAllOwnersItems(any()))
                .thenReturn(List.of(dtoWithDate));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchItems() throws Exception {
        when(service.searchItems(any()))
                .thenReturn(List.of(dto));

        mvc.perform(get("/items/search")
                        .param("text", "name")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateItem() throws Exception {
        when(service.updateItem(any(), any(), any()))
                .thenReturn(dto);

        mvc.perform(patch("/items/" + dtoWithDate.getId())
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dtoWithDate.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(dtoWithDate.getName())))
                .andExpect(jsonPath("$.description", is(dtoWithDate.getDescription())));
    }

    @Test
    void testSaveComment() throws Exception {
        when(service.createComment(any(), any(), any()))
                .thenReturn(CommentReturnDto.builder()
                        .id(1)
                        .text("text")
                        .itemId(dto.getId())
                        .authorName("name")
                        .build());

        Comment comment = new Comment();
        comment.setUserId(1);
        comment.setId(1);
        comment.setText("text");
        comment.setItemId(dto.getId());

        mvc.perform(post("/items/" + dto.getId() + "/comment")
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Integer.class))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is("name")));
    }

    @Test
    void testGetItemGetNotFound() throws Exception {
        when(service.getItem(any(), any()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get(("/items/" + dto.getId()))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().is(404));
    }

    @Test
    void testSaveCommentGetInvalidBook() throws Exception {
        when(service.createComment(any(), any(), any()))
                .thenThrow(InvalidBookingException.class);

        Comment comment = new Comment();
        comment.setUserId(1);
        comment.setId(1);
        comment.setText("text");
        comment.setItemId(dto.getId());

        mvc.perform(post("/items/" + dto.getId() + "/comment")
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is(400));
    }

    @Test
    void testUpdateItemGetInvalidHost() throws Exception {
        when(service.updateItem(any(), any(), any()))
                .thenThrow(InvalidHostException.class);

        mvc.perform(patch("/items/" + dtoWithDate.getId())
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is(403));
    }

    @Test
    void testGetItemGetThrowable() throws Exception {
        when(service.getItem(any(), any()))
                .thenThrow(RuntimeException.class);

        mvc.perform(get(("/items/" + dto.getId()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is(500));
    }

    @Test
    void testSaveItemGetCorruptedData() throws Exception {
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(ItemDto.builder().build()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is(400));
    }
}
