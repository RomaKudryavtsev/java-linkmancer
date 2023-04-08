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
    public ItemNoteDto add(@RequestBody ItemNoteDto itemNoteDto) {
        return noteService.addNote(itemNoteDto);
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
}
