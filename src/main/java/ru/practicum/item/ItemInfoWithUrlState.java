package ru.practicum.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemInfoWithUrlState implements ItemInfo {
    String url;
    Long id;
    HttpStatus state;

    public ItemInfoWithUrlState(ItemInfo item, HttpStatus state) {
        this.url = item.getUrl();
        this.id = item.getId();
        this.state = state;
    }
}
