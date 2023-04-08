package ru.practicum.item;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

interface ItemService {
    List<ItemDto> getItems(long userId);

    List<ItemInfo> getItemUrl(long userId);

    List<ItemInfoWithUrlState> getItemUrlWithState(long userId);

    ItemDto addNewItem(long userId, ItemDto itemDto);

    void deleteItem(long userId, long itemId);

    List<ItemCountByUser> countItemsByUser(String urlPart);

    List<ItemCountByUser> countByUserRegistered(LocalDate dateFrom, LocalDate dateTo);

    List<ItemDto> getItemsByUserIdAndTags(long userId, Set<String> tags);

    List<ItemDto> findItemsByLastNamePrefix(String lastNamePrefix);
}