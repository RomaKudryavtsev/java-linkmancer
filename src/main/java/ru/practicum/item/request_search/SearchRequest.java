package ru.practicum.item.request_search;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchRequest {
    Long userId;
    SearchState state;
    ContentType contentType;
    SortType sort;
    Integer limit;
    List<String> tags;

    public SearchRequest (Long userId, String state, String contentType, String sort, Integer limit, List<String> tags) {
        this.userId = userId;
        this.state = parseState(state);
        this.contentType = parseContentType(contentType);
        this.sort = parseSortType(sort);
        this.limit = limit;
        this.tags = tags;
    }

    private SearchState parseState(String state) {
        try {
            return SearchState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Wrong search state");
        }
    }

    private ContentType parseContentType(String contentType) {
        try {
            return ContentType.valueOf(contentType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Wrong content type");
        }

    }

    private SortType parseSortType(String sort) {
        try {
            return SortType.valueOf(sort);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Wrong sort type");
        }
    }
}
