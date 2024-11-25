package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.exception.CorruptedDataException;
import ru.practicum.shareit.booking.exception.InvalidHostException;
import ru.practicum.shareit.booking.exception.UnavailableItemException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.DuplicateDataException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:h2:mem:test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTests {
    private final EntityManager em;
    private final BookingService service;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void testGetBookingByCreator() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException, InvalidHostException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingReturnDto booking = service.addBooking(creteDto(item.getId()), user1.getId());

        BookingReturnDto result = service.getBooking(booking.getId(), user1.getId());

        assertThat(result.getId(), equalTo(booking.getId()));
        assertThat(result.getBooker().getId(), equalTo(booking.getBooker().getId()));
        assertThat(result.getItem().getId(), equalTo(booking.getItem().getId()));
        assertThat(result.getStart(), equalTo(booking.getStart()));
        assertThat(result.getEnd(), equalTo(booking.getEnd()));
    }

    @Test
    void testGetBookingByHost() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException, InvalidHostException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingReturnDto booking = service.addBooking(creteDto(item.getId()), user1.getId());

        BookingReturnDto result = service.getBooking(booking.getId(), user.getId());

        assertThat(result.getId(), equalTo(booking.getId()));
        assertThat(result.getBooker().getId(), equalTo(booking.getBooker().getId()));
        assertThat(result.getItem().getId(), equalTo(booking.getItem().getId()));
        assertThat(result.getStart(), equalTo(booking.getStart()));
        assertThat(result.getEnd(), equalTo(booking.getEnd()));
    }

    @Test
    void testGetBookingGetInvalidHost() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        UserDto user2 = createUser("u2@m.c", "name2");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        int id2 = userService.createUser(user2).getId();

        item = itemService.createItem(item, user.getId());

        BookingReturnDto booking = service.addBooking(creteDto(item.getId()), user1.getId());

        Assertions.assertThrows(InvalidHostException.class,
                () -> service.getBooking(booking.getId(), id2)
                );
    }

    @Test
    void testGetHostBookingsAll() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        service.addBooking(creteDto(item.getId()), user1.getId());

        assertThat(service.getHostBookings(user.getId(), State.ALL), notNullValue());
    }

    @Test
    void testGetHostBookingsCurrent() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        service.addBooking(creteDto(item.getId()), user1.getId());

        assertThat(service.getHostBookings(user.getId(), State.CURRENT), notNullValue());
    }

    @Test
    void testGetHostBookingsPast() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingDto dto = BookingDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        service.addBooking(dto, user1.getId());

        assertThat(service.getHostBookings(user.getId(), State.PAST), notNullValue());
    }

    @Test
    void testGetHostBookingsFuture() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingDto dto = BookingDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        service.addBooking(dto, user1.getId());

        assertThat(service.getHostBookings(user.getId(), State.FUTURE), notNullValue());
    }

    @Test
    void testGetHostBookingsWaiting() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        service.addBooking(creteDto(item.getId()), user1.getId());

        assertThat(service.getHostBookings(user.getId(), State.WAITING), notNullValue());
    }

    @Test
    void testGetUserBookingsAll() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        service.addBooking(creteDto(item.getId()), user1.getId());

        assertThat(service.getUserBookings(user1.getId(), State.ALL), notNullValue());
    }

    @Test
    void testGetUserBookingsCurrent() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        service.addBooking(creteDto(item.getId()), user1.getId());

        assertThat(service.getUserBookings(user1.getId(), State.CURRENT), notNullValue());
    }

    @Test
    void testGetUserBookingsPast() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingDto dto = BookingDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        service.addBooking(dto, user1.getId());

        assertThat(service.getUserBookings(user1.getId(), State.PAST), notNullValue());
    }

    @Test
    void testGetUserBookingsFuture() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingDto dto = BookingDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        service.addBooking(dto, user1.getId());

        assertThat(service.getUserBookings(user1.getId(), State.FUTURE), notNullValue());
    }

    @Test
    void testGetUserBookingsWaiting() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        service.addBooking(creteDto(item.getId()), user1.getId());

        assertThat(service.getUserBookings(user1.getId(), State.WAITING), notNullValue());
    }

    @Test
    void testAddBooking() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingReturnDto booking = service.addBooking(creteDto(item.getId()), user1.getId());

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where item.id = :id", Booking.class);
        Booking result = query.setParameter("id", item.getId())
                .getSingleResult();

        assertThat(result.getId(), equalTo(booking.getId()));
        assertThat(result.getBooker().getId(), equalTo(booking.getBooker().getId()));
        assertThat(result.getItem().getId(), equalTo(booking.getItem().getId()));
        assertThat(result.getStart(), equalTo(booking.getStart()));
        assertThat(result.getEnd(), equalTo(booking.getEnd()));
    }

    @Test
    void testAddBookingGetCorruptedData() throws DuplicateDataException, NotFoundException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingDto corruptedBooking = BookingDto.builder()
                .itemId(item.getId())
                .end(LocalDateTime.now().minusDays(2))
                .start(LocalDateTime.now().plusDays(2))
                .build();
        int userId = user1.getId();

        Assertions.assertThrows(ru.practicum.shareit.booking.exception.CorruptedDataException.class,
                () -> service.addBooking(corruptedBooking, userId)
                );
    }

    @Test
    void testUpdateStatusTrue() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException, InvalidHostException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingReturnDto booking = service.addBooking(creteDto(item.getId()), user1.getId());

        BookingReturnDto result = service.updateBookingStatus(booking.getId(), true, user.getId());

        assertThat(result.getId(), equalTo(booking.getId()));
        assertThat(result.getBooker().getId(), equalTo(booking.getBooker().getId()));
        assertThat(result.getItem().getId(), equalTo(booking.getItem().getId()));
        assertThat(result.getStart(), equalTo(booking.getStart()));
        assertThat(result.getEnd(), equalTo(booking.getEnd()));
        assertThat(result.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    void testUpdateStatusFalse() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException, InvalidHostException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingReturnDto booking = service.addBooking(creteDto(item.getId()), user1.getId());

        BookingReturnDto result = service.updateBookingStatus(booking.getId(), false, user.getId());

        assertThat(result.getId(), equalTo(booking.getId()));
        assertThat(result.getBooker().getId(), equalTo(booking.getBooker().getId()));
        assertThat(result.getItem().getId(), equalTo(booking.getItem().getId()));
        assertThat(result.getStart(), equalTo(booking.getStart()));
        assertThat(result.getEnd(), equalTo(booking.getEnd()));
        assertThat(result.getStatus(), equalTo(Status.REJECTED));
    }

    @Test
    void testUpdateStatusGetInvalidHost() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingReturnDto booking = service.addBooking(creteDto(item.getId()), user1.getId());
        int userId = user1.getId();

        Assertions.assertThrows(InvalidHostException.class,
                () -> service.updateBookingStatus(booking.getId(), true, userId)
        );
    }

    @Test
    void testUpdateStatusGetNotFound() throws DuplicateDataException, NotFoundException, ru.practicum.shareit.booking.exception.CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = createItem();

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        item = itemService.createItem(item, user.getId());

        BookingReturnDto booking = service.addBooking(creteDto(item.getId()), user1.getId());
        int userId = user.getId();

        Assertions.assertThrows(ru.practicum.shareit.booking.exception.NotFoundException.class,
                () -> service.updateBookingStatus(booking.getId() + 1, true, userId)
        );
    }

    @Test
    void testAddBookingGetUnavailable() throws DuplicateDataException, NotFoundException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = ItemDto.builder()
                .name("item")
                .description("desc")
                .available(false)
                .build();

        user = userService.createUser(user);
        int userId = userService.createUser(user1).getId();
        int itemId = itemService.createItem(item, user.getId()).getId();

        Assertions.assertThrows(UnavailableItemException.class,
                () -> service.addBooking(creteDto(itemId), userId)
        );
    }

    @Test
    void testGetUserBookingsGetNotFound() throws DuplicateDataException, NotFoundException, CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException {
        UserDto user = createUser("u@m.c", "name");
        UserDto user1 = createUser("u1@m.c", "name1");
        ItemDto item = ItemDto.builder()
                .name("item")
                .description("desc")
                .available(true)
                .build();

        user = userService.createUser(user);
        int userId = userService.createUser(user1).getId();
        service.addBooking(creteDto(itemService.createItem(item, user.getId()).getId()), userId);

        Assertions.assertThrows(ru.practicum.shareit.booking.exception.NotFoundException.class,
                () -> service.getUserBookings(userId + 1, State.ALL)
                );
    }

    private BookingDto creteDto(Integer id) {
        return BookingDto.builder()
                .itemId(id)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMonths(2))
                .build();
    }

    private UserDto createUser(String email, String name) {
        return UserDto.builder()
                .email(email)
                .name(name)
                .build();
    }

    private ItemDto createItem() {
        return ItemDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();
    }
}
