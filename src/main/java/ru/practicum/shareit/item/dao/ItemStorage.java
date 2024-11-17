package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ItemStorage extends JpaRepository<Item, Integer> {
    Collection<Item> findByHostId(int userId);

    @Query("select i from Item as i " +
            "where (upper(i.name) like upper(?1) or upper(i.description) like upper(?1)) and available = true")
    Collection<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String nameSearch);

    @Query("select i.hostId from Item as i where i.id = ?1")
    Integer findUserIdById(int id);

    @Query(value = "SELECT i.available FROM items AS i WHERE i.id = ?1", nativeQuery = true)
    Boolean findAvailableById(Integer id);

    @Query(value = "SELECT MAX(b.booking_end) FROM items as i" +
            " LEFT OUTER JOIN bookings as b ON b.item_id = i.id" +
            " WHERE i.id = ?1 AND b.booking_end < ?2" +
            " GROUP BY i.id", nativeQuery = true)
    @Nullable LocalDateTime findLastBooking(Integer itemId, LocalDateTime now);

    @Query(value = "SELECT MIN(b.start) FROM items as i" +
            " LEFT OUTER JOIN bookings as b ON b.item_id = i.id" +
            " WHERE i.id = ?1 AND b.start > ?2" +
            " GROUP BY i.id", nativeQuery = true)
    @Nullable LocalDateTime findNextBooking(Integer itemId, LocalDateTime now);
}
