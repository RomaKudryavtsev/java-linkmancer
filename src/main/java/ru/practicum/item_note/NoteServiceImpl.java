package ru.practicum.item_note;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final ItemRepository itemRepository;
    //private final UserRepository userRepository;

    //TODO: We need to use userId somehow - e.g., notes can be only by item's owner
    //Мы хотим, чтобы пользователь видел только те ссылки, которые он сам сохранил.
    @Override
    @Transactional
    public ItemNoteDto addNote(Long userId, ItemNoteDto itemNoteDto) {
        Instant now = Instant.now();
        ItemNote note = NoteMapper.mapToModel(itemNoteDto);
        note.setSaveDate(now);
        note.setItem(itemRepository.findById(itemNoteDto.getItemId()).orElseThrow());
        ItemNote savedNote = noteRepository.save(note);
        return NoteMapper.mapToDto(savedNote);
    }

    @Override
    public List<ItemNoteDto> searchNotes(Long userId, String url) {
        return noteRepository.findAllByItem_User_IdAndItem_UrlContaining(userId, url)
                .stream().map(NoteMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemNoteDto> searchNotesByTag(Long userId, String tag) {
        return noteRepository.findAllByUserIdAndTag(userId, tag).stream()
                .map(NoteMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemNoteDto> listAllItemsWithNotes(Long userId, int from, int size) {
        Pageable request = PageRequest.of(from > 0 ? from / size : 0, size);
        return noteRepository.findAllByItem_User_Id(userId, request).getContent().stream()
                .map(NoteMapper::mapToDto).collect(Collectors.toList());
    }
}
