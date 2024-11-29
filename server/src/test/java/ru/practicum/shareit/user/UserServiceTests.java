package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.DuplicateDataException;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:h2:mem:test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTests {
    private final EntityManager em;
    private final UserService service;

    @Test
    void testSaveUser() throws DuplicateDataException, NotFoundException {
        UserDto userDto = createUser("name", "m@m.m");
        int userId = service.createUser(userDto).getId();

        TypedQuery<User> query = em.createQuery("Select u from User u where id = :id", User.class);
        User user = query.setParameter("id", userId)
                .getSingleResult();

        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void testUpdateUser() throws DuplicateDataException, NotFoundException {
        UserDto userDto = createUser("name", "m@m.m");
        int userId = service.createUser(userDto).getId();
        userDto = createUser("nam2", "m2@m.m");
        service.updateUser(userDto, userId);

        TypedQuery<User> query = em.createQuery("Select u from User u where id = :id", User.class);
        User user = query.setParameter("id", userId)
                .getSingleResult();

        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void testGetUser() throws DuplicateDataException, NotFoundException {
        UserDto userDto = createUser("name", "m@m.m");
        int userId = service.createUser(userDto).getId();

        UserDto user = service.getUser(userId);

        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void testAddUserGetDuplicateData() throws DuplicateDataException, NotFoundException {
        UserDto userDto = createUser("name", "m@m.m");
        service.createUser(userDto);
        UserDto user = createUser("name", "m@m.m");

        Assertions.assertThrows(DuplicateDataException.class, () -> service.createUser(user));
    }

    @Test
    void testUpdateUserGetDuplicate() throws DuplicateDataException, NotFoundException {
        UserDto userDto = createUser("name", "m@m.m");
        int userId = service.createUser(userDto).getId();
        userDto = createUser("nam2", "m2@m.m");
        service.createUser(userDto);

        Assertions.assertThrows(DuplicateDataException.class,
                () -> service.updateUser(createUser("nam2", "m2@m.m"), userId)
        );
    }

    @Test
    void testGetUserGetNotFound() throws DuplicateDataException, NotFoundException {
        UserDto userDto = createUser("name", "m@m.m");
        int userId = service.createUser(userDto).getId();

        Assertions.assertThrows(NotFoundException.class,
                () -> service.getUser(userId + 1)
        );
    }

    @Test
    void testUpdateUserGetNotFound() throws DuplicateDataException, NotFoundException {
        UserDto userDto = createUser("name", "m@m.m");
        int userId = service.createUser(userDto).getId();

        Assertions.assertThrows(NotFoundException.class,
                () -> service.updateUser(createUser("name", "m2@mail.r"), userId + 1)
        );
    }

    @Test
    void testDeleteUser() throws DuplicateDataException, NotFoundException {
        UserDto userDto = createUser("name", "m@m.m");
        int userId = service.createUser(userDto).getId();
        service.deleteUser(userId);

        TypedQuery<User> query = em.createQuery("Select u from User u where id = :id", User.class);

        Assertions.assertThrows(NoResultException.class, () -> query.setParameter("id", userId).getSingleResult());
    }

    private UserDto createUser(String name, String email) {
        return UserDto.builder()
                .email(email)
                .name(name)
                .build();
    }
}
