package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.request_dates.DatesRequest;
import ru.practicum.item.request_modify.ModifyRequest;
import ru.practicum.item.request_search.SearchRequest;
import ru.practicum.item.request_tags.TagsRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/search")
    public List<ItemDto> searchWithFilters(@RequestHeader("X-Later-User-Id") long userId,
                                           @RequestParam(name = "state", defaultValue = "UNREAD") String state,
                                           @RequestParam(name = "content", defaultValue = "ALL") String content,
                                           @RequestParam(name = "sort", defaultValue = "NEWEST") String sort,
                                           @RequestParam(name = "limit", defaultValue = "10") int limit,
                                           @RequestParam(name = "tags", required = false) List<String> tags) {
        return itemService.searchWithFilters(new SearchRequest(userId, state, content, sort, limit, tags));
    }

    @PatchMapping(value = "/{id}/modify")
    public ItemDto modifyItem(@PathVariable("id") long itemId,
                              @RequestHeader("X-Later-User-Id") long userId,
                              @RequestParam(name = "replace", defaultValue = "true") Boolean replace,
                              @RequestParam(name = "unread", required = false, defaultValue = "false") Boolean unread,
                              @RequestBody(required = false) TagsRequest tagsRequest) {
        return itemService.modifyItem(ModifyRequest.builder()
                .itemId(itemId)
                .userId(userId)
                .replaceTags(replace)
                .unread(unread)
                .tags(tagsRequest)
                .build());
    }

    @DeleteMapping(value = "/{id}/delete")
    public String deleteById(@RequestHeader("X-Later-User-Id") Long userId, @PathVariable("id") Long itemId) {
        itemService.deleteById(userId, itemId);
        return "deleted";
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
    public ItemDto addItem(@RequestHeader("X-Later-User-Id") Long userId,
                           @RequestBody ItemDto item) {
        return itemService.addNewItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Later-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}