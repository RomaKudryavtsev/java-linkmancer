package ru.practicum.item_note;

public class NoteMapper {
    public static ItemNote mapToModel(ItemNoteDto itemNoteDto) {
        ItemNote itemNote = new ItemNote();
        itemNote.setText(itemNoteDto.getText());
        return itemNote;
    }

    public static ItemNoteDto mapToDto(ItemNote note) {
        return ItemNoteDto.builder()
                .id(note.getId())
                .text(note.getText())
                .itemId(note.getItem().getId())
                .itemUrl(note.getItem().getUrl())
                .saveDate(note.getSaveDate())
                .build();
    }
}
