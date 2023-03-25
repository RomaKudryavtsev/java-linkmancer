package ru.practicum.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ItemInfoWithUrlState implements ItemInfo {
    private String url;
    private Long id;
    private HttpStatus state;

    public ItemInfoWithUrlState(ItemInfo item, HttpStatus state) {
        this.url = item.getUrl();
        this.id = item.getId();
        this.state = state;
    }
}
