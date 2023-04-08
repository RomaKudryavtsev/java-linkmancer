package ru.practicum.item_note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<ItemNote, Long> {
    @EntityGraph(attributePaths = "item")
    List<ItemNote> findAllByItem_User_IdAndItem_UrlContaining(Long userId, String text);

    @Query("select note from ItemNote as note " +
            "join note.item as i join i.user as u " +
            "where u.id = ?1 and ?2 member of i.tags")
    @EntityGraph(attributePaths = "item")
    List<ItemNote> findAllByUserIdAndTag(Long userId, String tag);

    @EntityGraph(attributePaths = "item")
    Page<ItemNote> findAllByItem_User_Id(Long userId, Pageable page);
}
