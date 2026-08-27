package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.NotesDto;
import com.kodemi.model.Notes;

public interface NotesService {
    Notes createNote(NotesDto notesDto);
    String createNote(Notes notes);
    NotesDto getNoteId(String note_id);
    List<NotesDto> getAllNotes();
    String updateNotes(String note_id,Notes notes);
    String deleteNotes(String note_id);
}
