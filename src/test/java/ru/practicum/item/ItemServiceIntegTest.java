package ru.practicum.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.practicum.config.AppConfig;
import ru.practicum.config.PersistenceConfig;
import ru.practicum.config.WebConfig;
import ru.practicum.item.request_search.SearchRequest;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserService;
import ru.practicum.user.UserState;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringJUnitConfig({PersistenceConfig.class, ItemServiceImpl.class})
@ComponentScan(basePackages = {"ru.practicum"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, PersistenceConfig.class, WebConfig.class})
@WebAppConfiguration
public class ItemServiceIntegTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;

    private UserDto savedUser;
    private final List<ItemDto> itemDtoList = new ArrayList<>();

    @Autowired
    public ItemServiceIntegTest(EntityManager em, ItemService itemService, @Qualifier("userServiceImpl") UserService userService) {
        this.em = em;
        this.itemService = itemService;
        this.userService = userService;
    }

    @BeforeEach
    void beforeEach() {
        // given
        UserDto userDto = makeUserDto("some@email.com", "Peter", "Pan");
        ItemDto itemDto1 = makeItemDto("https://algotrading101.com/learn/deribit-api-guide/",
                Set.of("Trading", "IT"));
        ItemDto itemDto2 = makeItemDto("https://practicum.yandex.ru/java-developer/",
                Set.of("Education", "IT", "Java"));
        itemDtoList.addAll(List.of(itemDto1, itemDto2));
        // when
        savedUser = userService.saveUser(userDto);
        itemDtoList.forEach(itemDto -> itemService.addNewItem(savedUser.getId(), itemDto));
    }

    @Test
    void saveItem() {
        // then
        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.url = :url", Item.class);
        Item item = queryItem.setParameter("url", itemDtoList.get(0).getUrl())
                .getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getUrl(), equalTo(itemDtoList.get(0).getUrl()));
        assertThat(item.getMimeType(), equalTo("text"));
        assertThat(item.getHasImage(), equalTo(true));
        assertThat(item.getHasVideo(), equalTo(false));
        assertThat(item.getHasText(), equalTo(true));
        assertThat(item.getUnread(), equalTo(true));
        assertThat(item.getTags(), notNullValue());
    }

    @Test
    void searchItem() {
        SearchRequest request = makeSearchRequest(savedUser.getId(), "UNREAD", "ARTICLE", "NEWEST",
                5, List.of("Trading"));
        List<ItemDto> foundItems = itemService.searchWithFilters(request);
        assertThat(foundItems.size(), equalTo(1));
        assertThat(foundItems.get(0).getId(), notNullValue());
        assertThat(foundItems.get(0).getUrl(), equalTo(itemDtoList.get(0).getUrl()));
        assertThat(foundItems.get(0).getUserId(), equalTo(savedUser.getId()));
        assertThat(foundItems.get(0).getTags(), notNullValue());
    }

    private UserDto makeUserDto(String email, String firstName, String lastName) {
        UserDto dto = new UserDto();
        dto.setEmail(email);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setState(UserState.ACTIVE);
        return dto;
    }

    private ItemDto makeItemDto(String url, Set<String> tags) {
        ItemDto itemDto = new ItemDto();
        itemDto.setUrl(url);
        itemDto.setTags(tags);
        return itemDto;
    }

    private SearchRequest makeSearchRequest(Long userId, String state, String contentType, String sort,
                                            Integer limit, List<String> tags) {
        return new SearchRequest(userId, state, contentType, sort, limit, tags);
    }
}
