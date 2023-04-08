package ru.practicum.item_note;

import java.util.List;

public interface NoteService {
    ItemNoteDto addNote(Long userId, ItemNoteDto itemNoteDto);

    List<ItemNoteDto> searchNotes(Long userId, String url);

    List<ItemNoteDto> searchNotesByTag(Long userId, String tag);

    List<ItemNoteDto> listAllItemsWithNotes(Long userId, int from, int size);
}
