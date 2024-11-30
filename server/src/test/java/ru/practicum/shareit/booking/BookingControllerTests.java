package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.exception.InvalidHostException;
import ru.practicum.shareit.booking.exception.NotFoundException;
import ru.practicum.shareit.booking.exception.UnavailableItemException;
import ru.practicum.shareit.booking.handler.BookingErrorHandler;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTests {
    @Mock
    private BookingService service;
    @InjectMocks
    private BookingController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private BookingDto dto;
    private BookingReturnDto returnDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(BookingErrorHandler.class)
                .build();

        dto = BookingDto.builder()
                .id(1)
                .itemId(1)
                .booker(1)
                .build();

        returnDto = BookingReturnDto.builder()
                .id(1)
                .build();
    }

    @Test
    void testGetBooking() throws Exception {
        when(service.getBooking(any(), any()))
                .thenReturn(returnDto);

        mvc.perform(get("/bookings/" + dto.getItemId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Integer.class));
    }

    @Test
    void testGetUserBookings() throws Exception {
        when(service.getUserBookings(any(), any()))
                .thenReturn(List.of(returnDto));

        mvc.perform(get("/bookings")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void testGetHostBookings() throws Exception {
        when(service.getHostBookings(any(), any()))
                .thenReturn(List.of(returnDto));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void testAddBooking() throws Exception {
        when(service.addBooking(any(), any()))
                .thenReturn(returnDto);

        mvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(dto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Integer.class));
    }

    @Test
    void testUpdateStatus() throws Exception {
        when(service.updateBookingStatus(any(), anyBoolean(), any()))
                .thenReturn(returnDto);

        mvc.perform(patch("/bookings/" + dto.getId())
                .content(mapper.writeValueAsString(dto))
                .param("approved", "true")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Integer.class));
    }

    @Test
    void testAddBookingGetNotFound() throws Exception {
        when(service.addBooking(any(), any()))
                .thenThrow(NotFoundException.class);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    void testAddBookingGetUnavailable() throws Exception {
        when(service.addBooking(any(), any()))
                .thenThrow(UnavailableItemException.class);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400));
    }

    @Test
    void testAddBookingGetThrowable() throws Exception {
        when(service.addBooking(any(), any()))
                .thenThrow(RuntimeException.class);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(500));
    }

    @Test
    void testUpdateStatusGetInvalidHost() throws Exception {
        when(service.updateBookingStatus(any(), anyBoolean(), any()))
                .thenThrow(InvalidHostException.class);

        mvc.perform(patch("/bookings/" + dto.getId())
                        .content(mapper.writeValueAsString(dto))
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(403));
    }
}
