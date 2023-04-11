package ru.practicum.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.user.User;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column
    String url;

    // здесь остальные поля
    @Column(name = "resolved_url")
    String resolvedUrl;
    @Column(name = "mime_type")
    String mimeType;
    @Column(name = "title")
    String title;
    @Column(name = "has_image")
    Boolean hasImage;
    @Column(name = "has_video")
    Boolean hasVideo;
    @Column(name = "has_text")
    Boolean hasText;
    @Column(name = "unread")
    Boolean unread;
    @Column(name = "date_resolved")
    Instant dateResolved;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "name")
    Set<String> tags = new HashSet<>();

    public Item(User user, String url, Set<String> tags) {
        this.user = user;
        this.url = url;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}