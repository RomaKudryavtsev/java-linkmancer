package ru.practicum.item.request_modify;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.request_tags.TagsRequest;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModifyRequest {
    Long itemId;
    Long userId;
    TagsRequest tags;
    Boolean unread;
    Boolean replaceTags;
}
