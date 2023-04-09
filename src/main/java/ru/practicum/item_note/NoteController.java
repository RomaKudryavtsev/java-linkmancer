package ru.practicum.item_note;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public ItemNoteDto add(@RequestHeader("X-Later-User-Id") Long userId,
                           @RequestBody ItemNoteDto itemNoteDto) {
        return noteService.addNote(userId, itemNoteDto);
    }

    @GetMapping("/search")
    public List<ItemNoteDto> searchNotes(@RequestHeader("X-Later-User-Id") Long userId,
                                         @RequestParam("url") String url) {
        return noteService.searchNotes(userId, url);
    }

    @GetMapping("/search/tags")
    public List<ItemNoteDto> searchNotesByTag(@RequestHeader("X-Later-User-Id") Long userId,
                                              @RequestParam("tag") String tag) {
        return noteService.searchNotesByTag(userId, tag);
    }

    @GetMapping
    public List<ItemNoteDto> listAllNotes(@RequestHeader("X-Later-User-Id") Long userId,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        // возвращает набор пользовательских заметок, соответствующий указанным параметрам пагинации
        return noteService.listAllItemsWithNotes(userId, from, size);
    }
}
