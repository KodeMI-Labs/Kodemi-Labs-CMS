package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.NotesDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Notes;
import com.ContentManagementSystem.CMS.repository.NotesRepository;
import com.ContentManagementSystem.CMS.service.impl.NotesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotesServiceImplTest {

    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private NotesServiceImpl notesService;

    private Notes notes;

    @BeforeEach
    void setUp() {
        notes = new Notes();
        notes.setNote_id("n1");
        notes.setTitle("Test Note");
        notes.setDescription("Some description");
        notes.setVisibility("PUBLIC");
    }

    @Test
    void createNote_withDto_returnsNotes() {
        NotesDto dto = new NotesDto();
        dto.setNote_id("n1");
        dto.setTitle("Test Note");
        when(notesRepository.save(any(Notes.class))).thenAnswer(i -> i.getArgument(0));

        Notes result = notesService.createNote(dto);

        assertNotNull(result);
        assertEquals("Test Note", result.getTitle());
    }

    @Test
    void createNote_withModel_returnsSuccessMessage() {
        when(notesRepository.save(notes)).thenReturn(notes);

        String result = notesService.createNote(notes);

        assertEquals("Note Created Successfully", result);
    }

    @Test
    void getNoteId_found_returnsDto() {
        when(notesRepository.findById("n1")).thenReturn(notes);

        NotesDto result = notesService.getNoteId("n1");

        assertNotNull(result);
        assertEquals("Test Note", result.getTitle());
    }

    @Test
    void getNoteId_notFound_throwsException() {
        when(notesRepository.findById("n1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> notesService.getNoteId("n1"));
    }

    @Test
    void getAllNotes_returnsList() {
        when(notesRepository.findAll()).thenReturn(List.of(notes));

        List<NotesDto> result = notesService.getAllNotes();

        assertEquals(1, result.size());
    }

    @Test
    void updateNotes_found_returnsSuccessMessage() {
        when(notesRepository.findById("n1")).thenReturn(notes);
        when(notesRepository.save(any())).thenReturn(notes);

        String result = notesService.updateNotes("n1", notes);

        assertEquals("Notes Updated Successfully", result);
    }

    @Test
    void updateNotes_notFound_throwsException() {
        when(notesRepository.findById("n1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> notesService.updateNotes("n1", notes));
    }

    @Test
    void deleteNotes_found_returnsSuccessMessage() {
        when(notesRepository.findById("n1")).thenReturn(notes);

        String result = notesService.deleteNotes("n1");

        assertEquals("Notes Deleted Successfully", result);
        verify(notesRepository).delete("n1");
    }

    @Test
    void deleteNotes_notFound_throwsException() {
        when(notesRepository.findById("n1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> notesService.deleteNotes("n1"));
    }
}
