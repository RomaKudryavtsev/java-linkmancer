package ru.practicum.item_note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<ItemNote, Long> {
    List<ItemNote> findAllByItem_User_IdAndItem_UrlContaining(Long userId, String text);

    @Query("select note from ItemNote as note " +
            "join note.item as i join i.user as u " +
            "where u.id = ?1 and ?2 member of i.tags")
    List<ItemNote> findAllByUserIdAndTag(Long userId, String tag);
}
