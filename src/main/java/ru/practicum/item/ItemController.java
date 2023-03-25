package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
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