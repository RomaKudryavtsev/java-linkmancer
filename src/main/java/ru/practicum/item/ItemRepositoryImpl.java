package ru.practicum.item;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public List<Item> findByUserId(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public Item save(Item item) {
        item.setId(getId());
        items.compute(item.getUserId(), (userId, userItems) -> {
            if(userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });

        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if(items.containsKey(userId)) {
            List<Item> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
    }

    private long getId() {
        long lastId = items.values()
                .stream()
                .flatMap(Collection::stream)
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}