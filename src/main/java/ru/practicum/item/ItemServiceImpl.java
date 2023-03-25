package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public List<ItemCountByUser> countItemsByUser(String urlPart) {
        return repository.countItemsByUser(urlPart);
    }

    @Override
    public List<ItemCountByUser> countByUserRegistered(LocalDate dateFrom, LocalDate dateTo) {
        log.info("{} : {}", dateFrom, dateTo);
        return repository.countByUserRegistered(dateFrom, dateTo).stream()
                .map(row -> new ItemCountByUser(((BigInteger) row[0]).longValue(), ((BigInteger) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByUserIdAndTags(long userId, Set<String> tags) {
        BooleanExpression byUserId = QItem.item.userId.eq(userId);
        BooleanExpression byAnyTag = QItem.item.tags.any().in(tags);
        Iterable<Item> foundItems = repository.findAll(byUserId.and(byAnyTag));
        return ItemMapper.mapToItemDto(foundItems);
    }
}