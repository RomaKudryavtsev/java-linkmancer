package ru.practicum.item;

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
import ru.practicum.user.UserDto;
import ru.practicum.user.UserService;
import ru.practicum.user.UserState;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

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
public class ItemServiceImplTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemServiceImplTest(EntityManager em, ItemService itemService, @Qualifier("userServiceImpl") UserService userService) {
        this.em = em;
        this.itemService = itemService;
        this.userService = userService;
    }

    @Test
    void saveItem() {
        // given
        UserDto userDto = makeUserDto("some@email.com", "Peter", "Pan");
        ItemDto itemDto = new ItemDto();
        itemDto.setTags(Set.of("Trading", "IT"));
        itemDto.setUrl("https://algotrading101.com/learn/deribit-api-guide/");

        // when
        UserDto savedUserDto = userService.saveUser(userDto);
        itemService.addNewItem(savedUserDto.getId(), itemDto);

        // then
        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.url = :url", Item.class);
        Item item = queryItem.setParameter("url", itemDto.getUrl())
                .getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getUrl(), equalTo(itemDto.getUrl()));
        assertThat(item.getMimeType(), equalTo("text"));
        assertThat(item.getHasImage(), equalTo(true));
        assertThat(item.getHasVideo(), equalTo(false));
        assertThat(item.getHasText(), equalTo(true));
        assertThat(item.getUnread(), equalTo(true));
        assertThat(item.getTags(), notNullValue());
    }

    private UserDto makeUserDto(String email, String firstName, String lastName) {
        UserDto dto = new UserDto();
        dto.setEmail(email);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setState(UserState.ACTIVE);

        return dto;
    }
}
