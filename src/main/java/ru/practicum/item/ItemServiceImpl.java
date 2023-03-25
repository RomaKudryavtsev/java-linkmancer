package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public List<ItemDto> getItems(long userId) {
        List<Item> userItems = repository.findByUserId(userId);
        return ItemMapper.mapToItemDto(userItems);
    }

    @Override
    public List<ItemInfo> getItemUrl(long userId) {
        return repository.findUrlByUserId(userId);
    }

    @Override
    public List<ItemInfoWithUrlState> getItemUrlWithState(long userId) {
        return repository.findUrlAndStatusByUserId(userId);
    }

    @Transactional
    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        Item item = repository.save(ItemMapper.mapToItem(itemDto, userId));;
        return ItemMapper.mapToItemDto(item);
    }

    @Transactional
    @Override
    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndId(userId, itemId);
    }
}