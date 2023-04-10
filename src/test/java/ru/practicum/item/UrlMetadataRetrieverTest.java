package ru.practicum.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import ru.practicum.item.url_retriever.UrlMetadata;
import ru.practicum.item.url_retriever.UrlMetadataRetrieverClient;
import ru.practicum.item.url_retriever.UrlMetadataRetrieverImpl;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class UrlMetadataRetrieverTest {
    @Mock
    private UrlMetadataRetrieverClient client;

    @InjectMocks
    private UrlMetadataRetrieverImpl retriever;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.when(client.sendRequest(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new HttpResponse<Object>() {
                    @Override
                    public int statusCode() {
                        return HttpStatus.OK.value();
                    }

                    @Override
                    public HttpRequest request() {
                        return null;
                    }

                    @Override
                    public Optional<HttpResponse<Object>> previousResponse() {
                        return Optional.empty();
                    }

                    @Override
                    public HttpHeaders headers() {
                        Map<String, List<String>> headerMap = new HashMap<>();
                        List<String> contentType = new ArrayList<>();
                        contentType.add("image/jpeg");
                        headerMap.put("Content-Type", contentType);
                        return HttpHeaders.of(headerMap, (name, value) -> true);
                    }

                    @Override
                    public Object body() {
                        return "body";
                    }

                    @Override
                    public Optional<SSLSession> sslSession() {
                        return Optional.empty();
                    }

                    @Override
                    public URI uri() {
                        try {
                            return new URI("https://practicum.yandex.ru/java-developer/picture.jpeg");
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public HttpClient.Version version() {
                        return null;
                    }
                });
    }

    @Test
    public void retrieve() throws Exception {
        UrlMetadata meta = retriever.retrieve("https://practicum.yandex.ru/java-developer/picture.jpeg");
        assertThat(meta.getResolvedUrl(), equalTo("https://practicum.yandex.ru/java-developer/picture.jpeg"));
        assertThat(meta.getMimeType(), equalTo("image"));
        assertThat(meta.getNormalUrl(), equalTo("https://practicum.yandex.ru/java-developer/picture.jpeg"));
        assertThat(meta.getTitle(), notNullValue());
        assertThat(meta.getDateResolved(), notNullValue());
        assertTrue(meta.isHasImage());
    }
}
