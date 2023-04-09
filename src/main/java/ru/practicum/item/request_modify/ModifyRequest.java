package ru.practicum.item.request_modify;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModifyRequest {
    Long itemId;
    Long userId;
    Set<String> tags;
    Boolean unread;
    Boolean replaceTags;
}
