package ru.practicum.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String url;

    // здесь остальные поля
    @Column(name = "resolved_url")
    private String resolvedUrl;
    @Column(name = "mime_type")
    private String mimeType;
    @Column(name = "title")
    private String title;
    @Column(name = "has_image")
    private Boolean hasImage;
    @Column(name = "has_video")
    private Boolean hasVideo;
    @Column(name = "has_text")
    private Boolean hasText;
    @Column(name = "unread")
    private Boolean unread;
    @Column(name = "date_resolved")
    private Instant dateResolved;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "name")
    private Set<String> tags = new HashSet<>();

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