package ru.practicum.item.url_retriever;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import ru.practicum.exception.ItemRetrieverException;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Service
public class UrlMetadataRetrieverImpl implements UrlMetadataRetriever {
    private final UrlMetadataRetrieverClient client;

    @Autowired
    public UrlMetadataRetrieverImpl(UrlMetadataRetrieverClient client) {
        this.client = client;
    }

    @Override
    public UrlMetadata retrieve(String urlString) {
        final URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            throw new ItemRetrieverException("The URL is malformed: " + urlString, e);
        }

        // Сначала получаем информацию при помощи метода HEAD, он позволит получить часть информацию
        // без необходимости загружать контент
        HttpResponse<Void> resp = client.sendRequest(uri, "HEAD", HttpResponse.BodyHandlers.discarding());

        // Анализируем заголовки, полученного ответа, чтобы понять,
        // какой тип контента возвращает сервер по данному адресу
        String contentType = resp.headers()
                .firstValue(HttpHeaders.CONTENT_TYPE)
                .orElse("*");

        // Для удобства, воспользуемся вспомогательным классом из состава
        // Spring и преобразуем полученную строку в объект MediaType
        MediaType mediaType = MediaType.parseMediaType(contentType);

        final UrlMetadataImpl result;

        if (mediaType.isCompatibleWith(MimeType.valueOf("text/*"))) {
            result = handleText(resp.uri());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("image/*"))) {
            result = handleImage(resp.uri());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("video/*"))) {
            result = handleVideo(resp.uri());
        } else {
            throw new ItemRetrieverException("The content type [" + mediaType
                    + "] at the specified URL is not supported.");
        }

        return result.toBuilder()
                .normalUrl(urlString)
                .resolvedUrl(resp.uri().toString())
                .mimeType(mediaType.getType())
                .dateResolved(Instant.now())
                .build();
    }

    // Вспомогательный метод для получения метаданных о содержимом типа text
    // и его подтипов, это в том числе html и т.д.
    private UrlMetadataImpl handleText(URI url) {
        // Отправим get-запрос чтобы получить содержимое
        HttpResponse<String> resp = client.sendRequest(url, "GET", HttpResponse.BodyHandlers.ofString());

        // воспользуемся библиотекой Jsoup для парсинга содержимого
        Document doc = Jsoup.parse(resp.body());

        // ищем в полученном документе html-тэги, говорящие, что он
        // содержит видео или аудио информацию
        Elements imgElements = doc.getElementsByTag("img");
        Elements videoElements = doc.getElementsByTag("video");

        // добавляем полученные данные в ответ. В том числе находим заголовок
        // полученной страницы.
        return UrlMetadataImpl.builder()
                .title(doc.title())
                .hasText(true)
                .hasImage(!imgElements.isEmpty())
                .hasVideo(!videoElements.isEmpty())
                .build();
    }

    private UrlMetadataImpl handleVideo(URI url) {
        String name = new File(url).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasVideo(true)
                .build();
    }

    private UrlMetadataImpl handleImage(URI url) {
        String name = new File(url.getPath()).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasImage(true)
                .build();
    }
}
