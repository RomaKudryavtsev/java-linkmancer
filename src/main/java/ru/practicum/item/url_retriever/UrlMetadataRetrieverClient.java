package ru.practicum.item.url_retriever;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ItemRetrieverException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class UrlMetadataRetrieverClient {
    private final HttpClient client;

    public UrlMetadataRetrieverClient(@Value("${url-metadata-retriever.read_timeout-sec:120}") int readTimeout) {
        this.client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }

    public <T> HttpResponse<T> sendRequest (URI url,
                                        String method,
                                        HttpResponse.BodyHandler<T> responseBodyHandler) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();
        final HttpResponse<T> response;
        try {
            response = client.send(request, responseBodyHandler);
        } catch (IOException e) {
            throw new ItemRetrieverException("Cannot retrieve data from the URL: " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Cannot get the metadata for url: " + url
                    + " because the thread was interrupted.", e);
        }
        HttpStatus status = HttpStatus.resolve(response.statusCode());
        if (status == null) {
            throw new ItemRetrieverException("The server returned an unknown status code: " + response.statusCode());
        }
        if (status.equals(HttpStatus.UNAUTHORIZED) || status.equals(HttpStatus.FORBIDDEN)) {
            throw new ItemRetrieverException("There is no access to the resource at the specified URL: " + url);
        }
        if (status.isError()) {
            throw new ItemRetrieverException("Cannot get the data on the item because the server returned an error."
                    + "Response status: " + status);
        }
        return response;
    }
}
