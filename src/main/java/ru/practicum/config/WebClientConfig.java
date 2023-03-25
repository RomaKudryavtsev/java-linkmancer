package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean
    public ClientHttpConnector clientHttpConnector() {
        HttpClient httpClient = HttpClient.create();
        return new ReactorClientHttpConnector(httpClient);
    }
}
