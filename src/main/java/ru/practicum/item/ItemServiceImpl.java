package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.UserRepository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getItems(long userId) {
        List<Item> userItems = itemRepository.findByUserId(userId);
        return ItemMapper.mapToItemDto(userItems);
    }

    @Override
    public List<ItemInfo> getItemUrl(long userId) {
        return itemRepository.findUrlByUserId(userId);
    }

    @Override
    public List<ItemInfoWithUrlState> getItemUrlWithState(long userId) {
        return itemRepository.findUrlAndStatusByUserId(userId);
    }

    @Transactional
    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        Item inputItem = ItemMapper.mapToItem(itemDto);
        inputItem.setUser(userRepository.findById(userId).orElseThrow());
        Item outputItem = itemRepository.save(inputItem);
        return ItemMapper.mapToItemDto(outputItem);
    }

    @Transactional
    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    public List<ItemCountByUser> countItemsByUser(String urlPart) {
        return itemRepository.countItemsByUser(urlPart);
    }

    @Override
    public List<ItemCountByUser> countByUserRegistered(LocalDate dateFrom, LocalDate dateTo) {
        log.info("{} : {}", dateFrom, dateTo);
        return itemRepository.countByUserRegistered(dateFrom, dateTo).stream()
                .map(row -> new ItemCountByUser(((BigInteger) row[0]).longValue(), ((BigInteger) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByUserIdAndTags(long userId, Set<String> tags) {
        BooleanExpression byUserId = QItem.item.user.id.eq(userId);
        BooleanExpression byAnyTag = QItem.item.tags.any().in(tags);
        Iterable<Item> foundItems = itemRepository.findAll(byUserId.and(byAnyTag));
        return ItemMapper.mapToItemDto(foundItems);
    }

    @Override
    public List<ItemDto> findItemsByLastNamePrefix(String lastNamePrefix) {
        return itemRepository.findItemsByLastNamePrefix(lastNamePrefix).stream()
                .map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }

}