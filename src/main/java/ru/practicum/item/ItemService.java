package ru.practicum.item;

import java.util.List;

interface ItemService {
    List<ItemDto> getItems(long userId);

    List<ItemInfo> getItemUrl(long userId);

    List<ItemInfoWithUrlState> getItemUrlWithState(long userId);

    ItemDto addNewItem(long userId, ItemDto itemDto);

    void deleteItem(long userId, long itemId);
}