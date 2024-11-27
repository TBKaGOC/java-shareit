package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */

@Builder
@Getter
@Entity
@Table(name = "items")
@RequiredArgsConstructor
public class Item {
    public Item(Integer id, String name, String description, Integer hostId, Boolean available, Set<Comment> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.hostId = hostId;
        this.available = available;
        this.comments = comments;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Integer id;
    @NotBlank private String name;
    @NotBlank private String description;
    @Column(name = "user_id")
    @Setter
    private Integer hostId;
    @Setter
    private Boolean available;
    @OneToMany
    @JoinColumn(name = "itemId")
    private Set<Comment> comments;

}
