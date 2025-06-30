package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    public Booking(Integer id, LocalDateTime start, LocalDateTime end, Item item, User booker, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    Integer id;
    LocalDateTime start;
    @Column(name = "booking_end")
    LocalDateTime end;
    @Setter
    @ManyToOne(targetEntity = Item.class)
    @JoinColumn(name = "item_id")
    Item item;
    @Setter
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    User booker;
    @Setter
    @Enumerated(EnumType.STRING)
    Status status;
}
