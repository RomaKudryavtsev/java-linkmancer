package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ItemUrlService {
    private final WebClient webClient;

    @Autowired
    public ItemUrlService(ClientHttpConnector clientHttpConnector) {
        this.webClient = WebClient.builder().clientConnector(clientHttpConnector).build();
    }
    public Mono<HttpStatus> getStatusOfUrl(String url) {
        return webClient.get().uri(url).exchangeToMono(response -> Mono.just(response.statusCode()));
    }
}
