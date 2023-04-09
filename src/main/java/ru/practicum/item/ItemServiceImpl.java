package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.request_search.SearchRequest;
import ru.practicum.item.url_retriever.UrlMetadata;
import ru.practicum.item.url_retriever.UrlMetadataRetriever;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UrlMetadataRetriever retriever;

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
        User adder = userRepository.findById(userId).orElseThrow();
        UrlMetadata meta = retriever.retrieve(inputItem.getUrl());
        inputItem.setUser(adder);
        inputItem.setUrl(meta.getNormalUrl());
        inputItem.setResolvedUrl(meta.getResolvedUrl());
        inputItem.setMimeType(meta.getMimeType());
        inputItem.setTitle(meta.getTitle());
        inputItem.setHasImage(meta.isHasImage());
        inputItem.setHasVideo(meta.isHasVideo());
        inputItem.setDateResolved(meta.getDateResolved());
        inputItem.setUnread(true);
        inputItem.setHasText(meta.isHasText());

        Optional<Item> itemOpt = itemRepository.findByUserAndResolvedUrl(adder, meta.getResolvedUrl());
        Item outputItem;
        if(itemOpt.isEmpty()) {
            outputItem = itemRepository.save(inputItem);
        } else {
            inputItem = itemOpt.get();
            if(itemDto.getTags() != null && !itemDto.getTags().isEmpty()) {
                inputItem.getTags().addAll(itemDto.getTags());
                outputItem = itemRepository.save(inputItem);
            } else {
                outputItem = itemOpt.get();
            }
        }
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

    @Override
    public List<ItemDto> searchWithFilters(SearchRequest request) {
        BooleanExpression byUserId = QItem.item.user.id.eq(request.getUserId());
        BooleanExpression byUnread;
        switch (request.getState()) {
            case READ:
                byUnread = QItem.item.unread.isFalse();
                break;
            case UNREAD:
                byUnread = QItem.item.unread.isTrue();
                break;
            default:
                byUnread = QItem.item.unread.isFalse()
                        .or(QItem.item.unread.isTrue());
                break;
        }
        BooleanExpression byContentType;
        switch (request.getContentType()) {
            case ARTICLE:
                byContentType = QItem.item.hasText.isTrue();
                break;
            case IMAGE:
                byContentType = QItem.item.hasImage.isTrue();
                break;
            case VIDEO:
                byContentType = QItem.item.hasVideo.isTrue();
                break;
            default:
                byContentType = QItem.item.hasText.isTrue()
                        .or(QItem.item.hasImage.isTrue())
                        .or(QItem.item.hasVideo.isTrue());
                break;
        }
        BooleanExpression allConditions = byUserId.and(byUnread).and(byContentType);
        if(request.getTags() != null) {
            BooleanExpression byAnyTag = QItem.item.tags.any().in(request.getTags());
            allConditions = allConditions.and(byAnyTag);
        }
        Comparator<Item> comparator;
        switch(request.getSort()) {
            case NEWEST:
                comparator = Comparator.comparing(Item::getDateResolved).reversed();
                break;
            case OLDEST:
                comparator = Comparator.comparing(Item::getDateResolved);
                break;
            default:
                comparator = Comparator.comparing(Item::getTitle);
                break;
        }
        Pageable pageRequest = PageRequest.of(0, request.getLimit());
        return itemRepository.findAll(allConditions, pageRequest).getContent()
                .stream().sorted(comparator).map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }

}