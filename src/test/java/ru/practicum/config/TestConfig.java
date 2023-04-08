package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.user.UserService;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {
    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }
}
