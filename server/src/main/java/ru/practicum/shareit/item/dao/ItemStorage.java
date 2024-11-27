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
            "where (upper(i.name) like upper(:nameSearch) or upper(i.description) like upper(:nameSearch)) " +
            "and available = true")
    Collection<Item> findAvailableByQuery(String nameSearch);

    @Query("select i.hostId from Item as i where i.id = :id")
    Integer findUserIdById(int id);

    @Query(value = "SELECT i.available FROM items AS i WHERE i.id = :id", nativeQuery = true)
    Boolean findAvailableById(Integer id);

    @Query(value = "SELECT MAX(b.booking_end) FROM items as i" +
            " LEFT OUTER JOIN bookings as b ON b.item_id = i.id" +
            " WHERE i.id = :itemId AND b.booking_end < :now" +
            " GROUP BY i.id", nativeQuery = true)
    @Nullable LocalDateTime findLastBooking(Integer itemId, LocalDateTime now);

    @Query(value = "SELECT MIN(b.start) FROM items as i" +
            " LEFT OUTER JOIN bookings as b ON b.item_id = i.id" +
            " WHERE i.id = :itemId AND b.start > :now" +
            " GROUP BY i.id", nativeQuery = true)
    @Nullable LocalDateTime findNextBooking(Integer itemId, LocalDateTime now);
}
