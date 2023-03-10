package ru.practicum.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> itemMap = new HashMap<>();
    private long itemId = 0;

    @Override
    public List<Item> findByUserId(long userId) {
        return new ArrayList<>(itemMap.values());
    }

    @Override
    public Item save(Item item) {
        ++itemId;
        item.setId(itemId);
        itemMap.put(itemId, item);
        log.info("Item {} is added", item);
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if(itemMap.get(itemId).getUserId() == userId) {
            itemMap.remove(itemId);
        }
        log.info("Item having id={} was deleted", itemId);
    }
}
