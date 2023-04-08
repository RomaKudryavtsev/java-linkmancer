package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.practicum.config.AppConfig;
import ru.practicum.config.PersistenceConfig;
import ru.practicum.config.WebConfig;
import ru.practicum.user.User;
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
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringJUnitConfig( { PersistenceConfig.class, ItemServiceImpl.class})
@ComponentScan(basePackages = {"ru.practicum"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfig.class, PersistenceConfig.class, WebConfig.class })
@WebAppConfiguration
public class ItemServiceImplTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void saveItem() {
        // given
        UserDto userDto = makeUserDto("some@email.com", "Peter", "Pan");
        ItemDto itemDto = new ItemDto();
        itemDto.setTags(Set.of("Nature", "Animals"));
        itemDto.setUrl("google.com/animals");

        // when
        userService.saveUser(userDto);

        // then
        TypedQuery<User> queryUser = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = queryUser.setParameter("email", userDto.getEmail())
                .getSingleResult();

        itemService.addNewItem(user.getId(), itemDto);

        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.url = :url", Item.class);
        Item item = queryItem.setParameter("url", itemDto.getUrl())
                .getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getUrl(), equalTo(itemDto.getUrl()));
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