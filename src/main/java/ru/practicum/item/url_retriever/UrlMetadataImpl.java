package ru.practicum.item.url_retriever;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Value
@Builder(toBuilder = true)
public class UrlMetadataImpl implements UrlMetadata {
    String normalUrl;
    String resolvedUrl;
    String mimeType;
    String title;
    Boolean hasText;
    Boolean hasImage;
    Boolean hasVideo;
    Instant dateResolved;

    @Override
    public String getNormalUrl() {
        return normalUrl;
    }

    @Override
    public String getResolvedUrl() {
        return resolvedUrl;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isHasImage() {
        return hasImage;
    }

    @Override
    public boolean isHasVideo() {
        return hasVideo;
    }

    @Override
    public boolean isHasText() {
        return hasText;
    }

    @Override
    public Instant getDateResolved() {
        return dateResolved;
    }
}
