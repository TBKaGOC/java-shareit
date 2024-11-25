package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.CorruptedDataException;
import ru.practicum.shareit.user.exception.DuplicateDataException;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:h2:mem:test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {
    private final EntityManager em;
    private final ItemRequestService service;
    private final UserService userService;

    @Test
    void testSaveRequest() throws DuplicateDataException, CorruptedDataException, NotFoundException {
        ItemRequestDto dto = createDto("description");
        UserDto user = createUser("u@e.t", "Name");

        user = userService.createUser(user);
        service.addRequest(dto, user.getId());

        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i where i.description = :d", ItemRequest.class);
        ItemRequest request = query.setParameter("d", dto.getDescription())
                .getSingleResult();

        assertThat(request.getId(), CoreMatchers.notNullValue());
        assertThat(request.getDescription(), equalTo(dto.getDescription()));
        assertThat(request.getHostId(), equalTo(user.getId()));
    }

    @Test
    void testGetUserRequests() throws DuplicateDataException, CorruptedDataException, NotFoundException {
        ItemRequestDto dto = createDto("description");
        ItemRequestDto dto1 = createDto("description1");
        UserDto user = createUser("u@e.t", "Name");

        user = userService.createUser(user);
        service.addRequest(dto, user.getId());
        service.addRequest(dto1, user.getId());

        Collection<ItemRequestDto> res = service.getRequests(user.getId());

        assertThat(res, Matchers.hasSize(2));
    }

    @Test
    void testGetAll() throws DuplicateDataException, CorruptedDataException, NotFoundException {
        ItemRequestDto dto = createDto("description");
        ItemRequestDto dto1 = createDto("description1");
        UserDto user = createUser("u@e.t", "Name");
        UserDto user1 = createUser("u1@e.t", "Name1");

        user = userService.createUser(user);
        user1 = userService.createUser(user1);
        service.addRequest(dto, user.getId());
        service.addRequest(dto1, user1.getId());

        Collection<ItemRequestDto> res = service.getAll();

        assertThat(res, notNullValue());
    }

    @Test
    void testGetOne() throws DuplicateDataException, CorruptedDataException, NotFoundException, ru.practicum.shareit.request.exception.NotFoundException {
        ItemRequestDto dto = createDto("description");
        UserDto user = createUser("u@e.t", "Name");

        user = userService.createUser(user);
        dto = service.addRequest(dto, user.getId());

        ItemRequestDto request = service.getOne(dto.getId());

        assertThat(request.getId(), CoreMatchers.notNullValue());
        assertThat(request.getDescription(), equalTo(dto.getDescription()));
        assertThat(request.getHostId(), equalTo(user.getId()));
    }

    @Test
    void testGetOneGetNotFound() throws DuplicateDataException, CorruptedDataException, NotFoundException {
        ItemRequestDto dto = createDto("description");
        UserDto user = createUser("u@e.t", "Name");

        user = userService.createUser(user);
        dto = service.addRequest(dto, user.getId());
        int id = dto.getId() + 1;

        Assertions.assertThrows(ru.practicum.shareit.request.exception.NotFoundException.class,
            () -> service.getOne(id)
        );
    }

    private ItemRequestDto createDto(String description) {
        return ItemRequestDto.builder()
                .description(description)
                .build();
    }

    private UserDto createUser(String email, String name) {
        return UserDto.builder()
                .email(email)
                .name(name)
                .build();
    }
}
