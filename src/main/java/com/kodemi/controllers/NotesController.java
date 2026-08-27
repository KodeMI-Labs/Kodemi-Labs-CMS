package com.kodemi.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.NotesDto;
import com.kodemi.model.Notes;
import com.kodemi.service.NotesService;

import lombok.RequiredArgsConstructor;

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