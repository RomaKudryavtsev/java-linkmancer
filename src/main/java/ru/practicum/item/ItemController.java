package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.request_dates.DatesRequest;
import ru.practicum.item.request_search.SearchRequest;
import ru.practicum.item.request_tags.TagsRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/search")
    public List<ItemDto> searchWithFilters(@RequestHeader("X-Later-User-Id") long userId,
                             @RequestParam(defaultValue = "UNREAD") String state,
                             @RequestParam(defaultValue = "ALL") String contentType,
                             @RequestParam(defaultValue = "NEWEST") String sort,
                             @RequestParam(defaultValue = "10") int limit,
                             @RequestParam(required = false) List<String> tags) {
        return itemService.searchWithFilters(new SearchRequest(userId, state, contentType, sort, limit, tags));
    }

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Later-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/url")
    public List<ItemInfo> getUrl(@RequestHeader("X-Later-User-Id") long userId) {
        return itemService.getItemUrl(userId);
    }

    @GetMapping("/url/status")
    public List<ItemInfoWithUrlState> getUrlWithStatus(@RequestHeader("X-Later-User-Id") long userId) {
        return itemService.getItemUrlWithState(userId);
    }

    @GetMapping("/url/{urlPart}/count")
    public List<ItemCountByUser> getItemCountByUser(@PathVariable("urlPart") String urlPart) {
        return itemService.countItemsByUser(urlPart);
    }

    @GetMapping("/url/count/dates")
    public List<ItemCountByUser> countByUserRegistered(@RequestBody DatesRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate from = LocalDate.parse(request.getFrom(), formatter);
        LocalDate to = LocalDate.parse(request.getTo(), formatter);
        log.info("{} : {}", from, to);
        return itemService.countByUserRegistered(from, to);
    }

    @GetMapping("/search_prefix")
    public List<ItemDto> findItemByPrefix(@RequestParam("prefix") String prefix) {
        return itemService.findItemsByLastNamePrefix(prefix);
    }

    @GetMapping("/tags")
    public List<ItemDto> getItemsByUserIdAndTags(@RequestHeader("X-Later-User-Id") Long userId,
                                                 @RequestBody TagsRequest tags) {
        return itemService.getItemsByUserIdAndTags(userId, tags.getTags());
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Later-User-Id") Long userId,
                       @RequestBody ItemDto item) {
        return itemService.addNewItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Later-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}