package ru.practicum.item_note;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.ItemRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemNoteDto addNote(ItemNoteDto itemNoteDto) {
        Instant now = Instant.now();
        ItemNote note = NoteMapper.mapToModel(itemNoteDto);
        note.setSaveDate(now);
        note.setItem(itemRepository.findById(itemNoteDto.getItemId()).orElseThrow());
        ItemNote savedNote = noteRepository.save(note);
        return NoteMapper.mapToDto(savedNote);
    }

    @Override
    public List<ItemNoteDto> searchNotes(Long userId, String url) {
        return noteRepository.findAllByItem_User_IdAndItem_UrlContaining(userId, url).stream()
                .map(NoteMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemNoteDto> searchNotesByTag(Long userId, String tag) {
        return noteRepository.findAllByUserIdAndTag(userId, tag).stream()
                .map(NoteMapper::mapToDto).collect(Collectors.toList());
    }
}
