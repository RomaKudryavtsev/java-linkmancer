package ru.practicum.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.item.url_retriever.UrlMetadata;
import ru.practicum.item.url_retriever.UrlMetadataImpl;
import ru.practicum.item.url_retriever.UrlMetadataRetriever;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserState;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTest {
    @Mock
    ItemRepository itemRepositoryMock;
    @Mock
    UserRepository userRepositoryMock;
    @Mock
    UrlMetadataRetriever retrieverMock;
    @InjectMocks
    ItemServiceImpl itemService;
    Item testItem = new Item();
    User testUser = new User();
    UrlMetadata testMeta;
    Instant testNow = Instant.now();

    @BeforeEach
    public void setUp() {
        setMeta();
        setUser();
        setItem();
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
    public void searchItem() {

    }

    @Test
    public void updateItem() {

    }

    @Test
    public void deleteItem() {

    }
}
