package ru.practicum.item_note;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ItemNoteDto {
    Long id;
    String text;
    Long itemId;
    Instant saveDate;
    String itemUrl;
}
