package ru.practicum.shareit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.User;

public interface UserStorage extends JpaRepository<User, Integer> {
    @Query(value = "SELECT EXISTS(SELECT u.* FROM users As u WHERE email LIKE :text)", nativeQuery = true)
    boolean containsEmail(String text);

    @Query("select u.name from User as u where u.id = :userId")
    String getName(Integer userId);
}
