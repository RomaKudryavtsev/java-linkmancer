package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.request_dates.DatesRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

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