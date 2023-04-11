package ru.practicum.item;

import org.springframework.context.annotation.Lazy;
import ru.practicum.item.url_service.ItemUrlService;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final ItemRepository itemRepository;
    private final ItemUrlService itemUrlService;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository, ItemUrlService itemUrlService) {
        this.itemRepository = itemRepository;
        this.itemUrlService = itemUrlService;
    }

    @Override
    public List<ItemInfoWithUrlState> findUrlAndStatusByUserId(long userId) {
        return itemRepository.findUrlByUserId(userId).stream()
                .map(itemInfo -> new ItemInfoWithUrlState(itemInfo,
                        itemUrlService.getStatusOfUrl(itemInfo.getUrl()).block()))
                .collect(Collectors.toList());
    }
}
