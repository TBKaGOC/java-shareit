package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.exception.CorruptedDataException;
import ru.practicum.shareit.booking.exception.InvalidHostException;
import ru.practicum.shareit.booking.exception.UnavailableItemException;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.mapper.BookingReturnDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingStorage bookingStorage;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingDtoMapper mapper;
    private final BookingReturnDtoMapper returnMapper;

    public BookingReturnDto getBooking(Integer bookingId, Integer userId) throws NotFoundException, InvalidHostException {
        Booking booking = findBookingOrThrow(bookingId);

        if (isNotItemHost(userId, booking)
        && isNotBookingHost(userId, booking)) {
            throw new InvalidHostException("Пользователь " + userId +
                    " не является владельцем предмета " + booking.getItem().getId() +
                    " или бронирования " + booking.getId());
        }

        return returnMapper.mapToDto(booking);
    }

    public Collection<BookingReturnDto> getUserBookings(Integer userId, State state) throws NotFoundException {
        userService.throwNotFound(userId);

        Collection<Booking> bookings = new HashSet<>();
        LocalDate now = LocalDate.now();

        switch (state) {
            case ALL -> bookings = bookingStorage.getUserBooking(userId);
            case CURRENT -> bookings = bookingStorage.getUserBookingInProgress(userId, now);
            case PAST -> bookings = bookingStorage.getUserBookingInPast(userId, now);
            case FUTURE -> bookings = bookingStorage.getUserBookingFuture(userId, now);
            case WAITING, REJECTED -> bookings = bookingStorage.getUserBookingStatus(userId, state);
        }

        return bookings.stream().map(returnMapper::mapToDto).collect(Collectors.toSet());
    }

    public Collection<BookingReturnDto> getHostBookings(Integer userId, State state) throws NotFoundException {
        userService.throwNotFound(userId);

        Collection<Booking> bookings = new HashSet<>();
        LocalDate now = LocalDate.now();

        switch (state) {
            case ALL -> bookings = bookingStorage.getHostBooking(userId);
            case CURRENT -> bookings = bookingStorage.getHostBookingInProgress(userId, now);
            case PAST -> bookings = bookingStorage.getHostBookingInPast(userId, now);
            case FUTURE -> bookings = bookingStorage.getHostBookingFuture(userId, now);
            case WAITING, REJECTED -> bookings = bookingStorage.getHostBookingStatus(userId, state);
        }

        return bookings.stream().map(returnMapper::mapToDto).collect(Collectors.toSet());
    }

    public BookingReturnDto addBooking(BookingDto booking, Integer bookerId) throws NotFoundException,
            CorruptedDataException,
            UnavailableItemException, ru.practicum.shareit.item.exception.NotFoundException {
        userService.throwNotFound(bookerId);
        itemService.throwNotFound(booking.getItemId());
        itemService.throwNotAvailable(booking.getItemId());
        if (booking.getEnd().equals(booking.getStart()) || booking.getEnd().isBefore(booking.getStart())) {
            throw new CorruptedDataException("Дата старта должна быть раньше даты конца");
        }

        Booking b = mapper.mapToBooking(booking);
        b.setStatus(Status.WAITING);
        b.setBooker(userService.findUserOrThrow(bookerId));
        b.setItem(itemService.findItemOrThrow(booking.getItemId()));

        return returnMapper.mapToDto(bookingStorage.save(b));
    }

    public BookingReturnDto updateBookingStatus(Integer bookingId, boolean approved, Integer bookerId)
            throws NotFoundException, InvalidHostException {
        Booking booking = findBookingOrThrow(bookingId);

        if (isNotItemHost(bookerId, booking)) {
            throw new InvalidHostException("Пользователь " + bookerId +
                    " не является владельцем предмета " + booking.getItem());
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return returnMapper.mapToDto(bookingStorage.save(booking));
    }

    private static boolean isNotBookingHost(Integer userId, Booking booking) {
        return !Objects.equals(booking.getBooker().getId(), userId);
    }

    private boolean isNotItemHost(Integer userId, Booking booking) {
        return !Objects.equals(itemService.findHost(booking.getItem().getId()), userId);
    }

    private Booking findBookingOrThrow(Integer bookingId) throws NotFoundException {
        return bookingStorage.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Бронирование " + bookingId + " не существует")
        );
    }
}
