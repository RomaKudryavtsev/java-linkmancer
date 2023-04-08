package ru.practicum.item_note;

import java.util.List;

public interface NoteService {
    ItemNoteDto addNote(ItemNoteDto itemNoteDto);

    List<ItemNoteDto> searchNotes(Long userId, String url);

    List<ItemNoteDto> searchNotesByTag(Long userId, String tag);
}
