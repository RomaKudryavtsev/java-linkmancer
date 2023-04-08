package ru.practicum.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.config.AppConfig;
import ru.practicum.config.PersistenceConfig;
import ru.practicum.config.TestConfig;
import ru.practicum.config.WebConfig;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig({ UserController.class, TestConfig.class, WebConfig.class, AppConfig.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { AppConfig.class, PersistenceConfig.class, WebConfig.class })
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTestWithContext {
    private final ObjectMapper mapper = new ObjectMapper();

    private final UserService userService;

    private MockMvc mvc;

    private UserDto userDto;

    @Autowired
    UserControllerTestWithContext(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        userDto = new UserDto(
                1L,
                "john.doe@mail.com",
                "John",
                "Doe",
                "2022.07.03 19:55:00",
                UserState.ACTIVE);
    }

    @Test
    void saveNewUser() throws Exception {
        when(userService.saveUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void saveNewUserWithException() throws Exception {
        when(userService.saveUser(any()))
                .thenThrow(new IllegalArgumentException());

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("Invalid argument")));
    }
}
