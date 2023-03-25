package ru.practicum.item.request_tags;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class TagsRequest {
    private Set<String> tags;
}
