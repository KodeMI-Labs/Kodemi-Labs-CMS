package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.NotesDto;
import com.ContentManagementSystem.CMS.model.Notes;

import java.util.List;

public interface NotesService {
    Notes createNote(NotesDto notesDto);
    String createNote(Notes notes);
    NotesDto getNoteId(String note_id);
    List<NotesDto> getAllNotes();
    String updateNotes(String note_id,Notes notes);
    String deleteNotes(String note_id);
}
