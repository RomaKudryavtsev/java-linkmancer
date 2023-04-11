package ru.practicum.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.item.request_modify.ModifyRequest;
import ru.practicum.item.request_tags.TagsRequest;
import ru.practicum.item.url_retriever.UrlMetadata;
import ru.practicum.item.url_retriever.UrlMetadataImpl;
import ru.practicum.item.url_retriever.UrlMetadataRetriever;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserState;

import java.time.Instant;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceUnitTest {
    @Mock
    ItemRepository itemRepositoryMock;
    @Mock
    UserRepository userRepositoryMock;
    @Mock
    UrlMetadataRetriever retrieverMock;
    @InjectMocks
    ItemServiceImpl itemService;
    final Item testItem = new Item();
    final User testUser = new User();
    UrlMetadata testMeta;
    final Instant testNow = Instant.now();
    ItemCountByUser count;


    @BeforeEach
    public void setUp() {
        setMeta();
        setUser();
        setItem();
        count = new ItemCountByUser(1L, 1L);
    }

    private void setMeta() {
        testMeta = UrlMetadataImpl.builder()
                .normalUrl("https://practicum.yandex.ru/java-developer/")
                .resolvedUrl("https://practicum.yandex.ru/java-developer/")
                .mimeType("text")
                .title(" урс Java-разработчик с нул€: онлайн-обучение Java-программированию дл€ начинающих Ч " +
                        "яндекс ѕрактикум")
                .hasText(true)
                .hasImage(true)
                .hasVideo(false)
                .dateResolved(testNow)
                .build();
    }

    private void setUser() {
        testUser.setId(1L);
        testUser.setEmail("test@gmail.com");
        testUser.setFirstName("Test");
        testUser.setLastName("Test");
        testUser.setRegistrationDate(testNow);
        testUser.setState(UserState.ACTIVE);
    }

    private void setItem() {
        testItem.setId(1L);
        testItem.setUser(testUser);
        testItem.setUrl(testMeta.getNormalUrl());
        testItem.setResolvedUrl(testMeta.getResolvedUrl());
        testItem.setMimeType(testMeta.getMimeType());
        testItem.setTitle(testMeta.getTitle());
        testItem.setHasText(testMeta.isHasText());
        testItem.setHasImage(testMeta.isHasImage());
        testItem.setHasVideo(testMeta.isHasVideo());
        testItem.setUnread(false);
        testItem.setDateResolved(testMeta.getDateResolved());
        testItem.setTags(Set.of("Education", "IT"));
    }

    @Test
    public void saveItem() {
        Mockito.when(itemRepositoryMock.save(Mockito.any())).thenReturn(testItem);
        Mockito.when(retrieverMock.retrieve(Mockito.anyString())).thenReturn(testMeta);
        Mockito.when(userRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));
        Long userId = 1L;
        ItemDto input = new ItemDto();
        input.setUrl("https://practicum.yandex.ru/java-developer/");
        input.setTags(Set.of("Education", "IT"));
        ItemDto output = itemService.addNewItem(userId, input);
        assertThat(output.getId(), equalTo(1L));
        assertThat(output.getUserId(), equalTo(1L));
        assertThat(output.getUrl(), equalTo("https://practicum.yandex.ru/java-developer/"));
        assertThat(output.getTags(), hasSize(2));
    }

    @Test
    public void getItems() {
        Mockito.when(itemRepositoryMock.findByUserId(Mockito.anyLong())).thenReturn(List.of(testItem));
        assertThat(itemService.getUsersItems(1L), hasSize(1));
    }

    @Test
    public void countItemsByUser() {
        Mockito.when(itemRepositoryMock.countItemsByUser(Mockito.anyString())).thenReturn(List.of(count));
        assertThat(itemService.countItemsByUser("test"), hasSize(1));
    }

    @Test
    public void findItemsByLastNamePrefix() {
        Mockito.when(itemRepositoryMock.findItemsByLastNamePrefix(Mockito.anyString())).thenReturn(List.of(testItem));
        assertThat(itemService.findItemsByLastNamePrefix("test"), hasSize(1));
    }

    @Test
    public void updateItemRewriteTags() {
        testItem.setTags(Set.of("test"));
        Mockito.when(itemRepositoryMock.save(Mockito.any())).thenReturn(testItem);
        Mockito.when(itemRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(testItem));
        TagsRequest tagsRequest = new TagsRequest();
        tagsRequest.setTags(Set.of("test"));
        ModifyRequest modifyRequest = ModifyRequest.builder()
                .itemId(1L)
                .userId(1L)
                .tags(tagsRequest)
                .unread(false)
                .replaceTags(true)
                .build();
        assertThat(itemService.modifyItem(modifyRequest).getTags(), hasSize(1));
    }

    @Test
    public void updateItemAddTags() {
        Set<String> tags = new HashSet<>(Set.of("Education", "IT", "test"));
        testItem.setTags(tags);
        Mockito.when(itemRepositoryMock.save(Mockito.any())).thenReturn(testItem);
        Mockito.when(itemRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(testItem));
        TagsRequest tagsRequest = new TagsRequest();
        tagsRequest.setTags(Set.of("test"));
        ModifyRequest modifyRequest = ModifyRequest.builder()
                .itemId(1L)
                .userId(1L)
                .tags(tagsRequest)
                .unread(false)
                .replaceTags(false)
                .build();
        assertThat(itemService.modifyItem(modifyRequest).getTags(), hasSize(3));
    }

    @Test
    public void deleteItem() {
        Mockito.when(itemRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(testItem));
        itemService.deleteById(1L, 1L);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void getItemUrl() {
        List<ItemInfo> expectedItemInfos = Arrays.asList(
                new ItemInfoImpl(1L, "http://example.com/item/1"),
                new ItemInfoImpl(2L, "http://example.com/item/2")
        );
        Mockito.when(itemRepositoryMock.findUrlByUserId(Mockito.anyLong())).thenReturn(expectedItemInfos);
        assertThat(expectedItemInfos, equalTo(itemService.getItemUrl(1L)));
    }

    private static class ItemInfoImpl implements ItemInfo {
        private final Long id;
        private final String url;

        ItemInfoImpl(Long id, String url) {
            this.id = id;
            this.url = url;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }
}
