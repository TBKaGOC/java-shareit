package ru.practicum.shareit.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    Collection<ItemRequest> findByHostIdOrderByCreatedDesc(Integer hostId);

    Collection<ItemRequest> findAllByOrderByCreatedDesc();
}
