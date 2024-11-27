package ru.practicum.shareit.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */

@Builder
@Entity
@Getter
@Table(name = "requests")
@RequiredArgsConstructor
public class ItemRequest {
    public ItemRequest(Integer id, String description, Integer hostId, LocalDateTime created, Set<Item> items) {
        this.id = id;
        this.description = description;
        this.hostId = hostId;
        this.created = created;
        this.items = items;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotNull
    String description;
    @Column(name = "user_id")
    Integer hostId;
    LocalDateTime created;
    @OneToMany
    @JoinColumn(name = "request_id")
    Set<Item> items;
}
