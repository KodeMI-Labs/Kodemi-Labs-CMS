package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.NotesDto;
import com.ContentManagementSystem.CMS.model.Notes;
import com.ContentManagementSystem.CMS.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NotesController {
    private final NotesService notesService;
    @PostMapping("/create-dto")
    public Notes createNoteDto(
            @RequestBody NotesDto notesDto) {
        return notesService.createNote(notesDto);
    }
    @PostMapping("/create")
    public String createNote(
            @RequestBody Notes notes) {
        return notesService.createNote(notes);
    }
    @GetMapping("/{note_id}")
    public NotesDto getNoteById(
            @PathVariable String note_id) {
        return notesService.getNoteId(note_id);
    }
    @GetMapping("/all")
    public List<NotesDto> getAllNotes() {
        return notesService.getAllNotes();
    }
    @PutMapping("/update/{note_id}")
    public String updateNotes(
            @PathVariable String note_id,
            @RequestBody Notes notes) {
        return notesService.updateNotes(
                note_id,
                notes
        );
    }
    @DeleteMapping("/delete/{note_id}")
    public String deleteNotes(
            @PathVariable String note_id) {
        return notesService.deleteNotes(note_id);
    }
}