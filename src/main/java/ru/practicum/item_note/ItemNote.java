package ru.practicum.item_note;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.item.Item;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "item_notes")
@Getter @Setter @ToString
@NoArgsConstructor
public class ItemNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    Item item;
    @Column(name = "save_date")
    private Instant saveDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemNote)) return false;
        return id != null && id.equals(((ItemNote) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
