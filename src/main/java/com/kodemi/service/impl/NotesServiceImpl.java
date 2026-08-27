package com.kodemi.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.NotesDto;
import com.kodemi.model.Notes;
import com.kodemi.repository.NotesRepository;
import com.kodemi.service.NotesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotesServiceImpl implements NotesService {
    private final NotesRepository notesRepository;
    @Override
    public Notes createNote(NotesDto notesDto) {
        Notes notes = new Notes();
        BeanUtils.copyProperties(notesDto, notes);
        notes.setNote_id(UUID.randomUUID().toString());
        notes.setCreated_at(LocalDateTime.now());
        notes.setUpdated_at(LocalDateTime.now());
        notesRepository.save(notes);
        return notes;
    }
    @Override
    public String createNote(Notes notes) {
        notes.setNote_id(UUID.randomUUID().toString());
        notes.setCreated_at(LocalDateTime.now());
        notes.setUpdated_at(LocalDateTime.now());
        notesRepository.save(notes);
        return "Note Created Successfully";
    }
    @Override
    public NotesDto getNoteId(String note_id) {
        Notes notes = notesRepository.findById(note_id);
        if (notes == null) {
            throw new ResourceNotFoundException("Note not found with id: " + note_id);
        }
        NotesDto notesDto = new NotesDto();
        BeanUtils.copyProperties(notes, notesDto);
        return notesDto;
    }
    @Override
    public List<NotesDto> getAllNotes() {
        List<Notes> notesList = notesRepository.findAll();
        List<NotesDto> notesDtoList = new ArrayList<>();
        for (Notes notes : notesList) {
            NotesDto notesDto = new NotesDto();
            BeanUtils.copyProperties(notes, notesDto);
            notesDtoList.add(notesDto);
        }
        return notesDtoList;
    }
    @Override
    public String updateNotes(String note_id, Notes notes) {
        Notes existingNotes = notesRepository.findById(note_id);
        if (existingNotes == null) {
            throw new ResourceNotFoundException("Note not found with id: " + note_id);
        }
        existingNotes.setTitle(notes.getTitle());
        existingNotes.setDescription(notes.getDescription());
        existingNotes.setAttachment_url(notes.getAttachment_url());
        existingNotes.setVisibility(notes.getVisibility());
        existingNotes.setUpdated_at(LocalDateTime.now());
        notesRepository.save(existingNotes);
        return "Notes Updated Successfully";
    }
    @Override
    public String deleteNotes(String note_id) {
        Notes notes = notesRepository.findById(note_id);
        if (notes == null) {
            throw new ResourceNotFoundException("Note not found with id: " + note_id);
        }
        notesRepository.delete(note_id);
        return "Notes Deleted Successfully";
    }
}