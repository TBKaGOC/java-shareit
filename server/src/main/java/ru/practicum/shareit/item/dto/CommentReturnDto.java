package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class CommentReturnDto {
    Integer id;
    Integer itemId;
    String authorName;
    LocalDate created;
    String text;
}
