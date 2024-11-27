package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.CorruptedDataException;
import ru.practicum.shareit.booking.exception.UnavailableItemException;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.exception.InvalidBookingException;
import ru.practicum.shareit.item.exception.InvalidHostException;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.DuplicateDataException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:h2:mem:test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTests {
    private final EntityManager em;
    private final ItemService service;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void testSaveItem() throws DuplicateDataException, NotFoundException {
        ItemDto dto = createDto();
        UserDto user = createUser();

        user = userService.createUser(user);
        int itemId = service.createItem(dto, user.getId()).getId();

        TypedQuery<Item> query = em.createQuery("Select i from Item i where id = :itemId", Item.class);
        Item item = query.setParameter("itemId", itemId)
                .getSingleResult();

        assertThat(item.getId(), equalTo(itemId));
        assertThat(item.getName(), equalTo(dto.getName()));
        assertThat(item.getDescription(), equalTo(dto.getDescription()));
    }

    @Test
    void testGetUserItems() throws DuplicateDataException, NotFoundException {
        ItemDto dto = createDto();
        UserDto user = createUser();

        user = userService.createUser(user);
        service.createItem(dto, user.getId());

        Collection<ItemDtoWithDate> res = service.getAllOwnersItems(user.getId());

        assertThat(res, notNullValue());
    }

    @Test
    void testGetItem() throws DuplicateDataException, NotFoundException {
        ItemDto dto = createDto();
        UserDto user = createUser();

        user = userService.createUser(user);
        int itemId = service.createItem(dto, user.getId()).getId();

        ItemDtoWithDate item = service.getItem(itemId, user.getId());

        assertThat(item.getId(), equalTo(itemId));
        assertThat(item.getName(), equalTo(dto.getName()));
        assertThat(item.getDescription(), equalTo(dto.getDescription()));
    }

    @Test
    void testUpdateItem() throws DuplicateDataException, NotFoundException, InvalidHostException {
        ItemDto dto = createDto();
        UserDto user = createUser();

        user = userService.createUser(user);
        int itemId = service.createItem(dto, user.getId()).getId();

        dto = ItemDto.builder().name("nameNew").build();

        ItemDto item = service.updateItem(dto, itemId, user.getId());

        assertThat(item.getId(), equalTo(itemId));
        assertThat(item.getName(), equalTo(dto.getName()));
    }

    @Test
    void testSearchItems() throws DuplicateDataException, NotFoundException {
        ItemDto dto = createDto();
        UserDto user = createUser();

        user = userService.createUser(user);
        service.createItem(dto, user.getId());

        Collection<ItemDto> res = service.searchItems(dto.getName());

        assertThat(res, notNullValue());
    }

    @Test
    void  testCreateComment() throws DuplicateDataException, NotFoundException, InvalidBookingException, CorruptedDataException, ru.practicum.shareit.booking.exception.NotFoundException, UnavailableItemException, ru.practicum.shareit.booking.exception.InvalidHostException {
        ItemDto dto = createDto();
        UserDto user = createUser();

        user = userService.createUser(user);
        dto = service.createItem(dto, user.getId());

        Comment comment = new Comment();
        comment.setText("text");

        int bookingId = bookingService.addBooking(
                BookingDto.builder()
                        .start(LocalDateTime.now().minusDays(1))
                        .end(LocalDateTime.now().minusHours(2))
                        .itemId(dto.getId())
                        .build(),
                user.getId()
        ).getId();
        bookingService.updateBookingStatus(bookingId, true, user.getId());

        int commentId = service.createComment(comment, dto.getId(), user.getId()).getId();

        TypedQuery<Comment> query = em.createQuery("Select c from Comment c where id = :id", Comment.class);
        Comment result = query.setParameter("id", commentId)
                .getSingleResult();

        assertThat(result.getId(), equalTo(commentId));
        assertThat(result.getText(), equalTo(comment.getText()));
        assertThat(result.getItemId(), equalTo(dto.getId()));
        assertThat(result.getUserId(), equalTo(user.getId()));
    }

    @Test
    void  testCreateCommentGetInvalidBook() throws DuplicateDataException, NotFoundException {
        ItemDto dto = createDto();
        UserDto user = createUser();

        int userId = userService.createUser(user).getId();
        int itemId = service.createItem(dto, userId).getId();

        Comment comment = new Comment();
        comment.setText("text");

        Assertions.assertThrows(InvalidBookingException.class,
                () -> service.createComment(comment, itemId, userId)
        );
    }


    private ItemDto createDto() {
        return ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    private UserDto createUser() {
        return UserDto.builder()
                .email("email@m.m")
                .name("name")
                .build();
    }
}
