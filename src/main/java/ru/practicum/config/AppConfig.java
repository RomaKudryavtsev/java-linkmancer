package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.practicum.item.ItemCountByUserConverter;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean
    public ItemCountByUserConverter itemCountByUserConverter() {
        return new ItemCountByUserConverter();
    }
}