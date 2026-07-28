package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.NotesDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Notes;
import com.ContentManagementSystem.CMS.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.ContentManagementSystem.CMS.config.JwtUtil;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotesController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotesService notesService;

    @MockBean
    private JwtUtil jwtUtil;

    private Notes notes;
    private NotesDto notesDto;

    @BeforeEach
    void setUp() {
        notes = new Notes();
        notes.setNote_id("n1");
        notes.setTitle("My Note");
        notes.setDescription("Some description");
        notes.setVisibility("PUBLIC");

        notesDto = new NotesDto();
        notesDto.setNote_id("n1");
        notesDto.setTitle("My Note");
        notesDto.setDescription("Some description");
        notesDto.setVisibility("PUBLIC");
    }

    @Test
    void createNote_returnsSuccessMessage() throws Exception {
        when(notesService.createNote(any(Notes.class))).thenReturn("Note Created Successfully");

        mockMvc.perform(post("/notes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notes)))
                .andExpect(status().isOk())
                .andExpect(content().string("Note Created Successfully"));
    }

    @Test
    void createNoteDto_returnsNotes() throws Exception {
        when(notesService.createNote(any(NotesDto.class))).thenReturn(notes);

        mockMvc.perform(post("/notes/create-dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note_id").value("n1"))
                .andExpect(jsonPath("$.title").value("My Note"));
    }

    @Test
    void getNoteById_found_returnsDto() throws Exception {
        when(notesService.getNoteId("n1")).thenReturn(notesDto);

        mockMvc.perform(get("/notes/n1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note_id").value("n1"))
                .andExpect(jsonPath("$.title").value("My Note"));
    }

    @Test
    void getNoteById_notFound_returns404() throws Exception {
        when(notesService.getNoteId("n1")).thenThrow(new ResourceNotFoundException("Note not found with id: n1"));

        mockMvc.perform(get("/notes/n1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Note not found with id: n1"));
    }

    @Test
    void getAllNotes_returnsList() throws Exception {
        when(notesService.getAllNotes()).thenReturn(List.of(notesDto));

        mockMvc.perform(get("/notes/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateNotes_found_returnsSuccessMessage() throws Exception {
        when(notesService.updateNotes(eq("n1"), any(Notes.class))).thenReturn("Notes Updated Successfully");

        mockMvc.perform(put("/notes/update/n1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notes)))
                .andExpect(status().isOk())
                .andExpect(content().string("Notes Updated Successfully"));
    }

    @Test
    void updateNotes_notFound_returns404() throws Exception {
        when(notesService.updateNotes(eq("n1"), any(Notes.class)))
                .thenThrow(new ResourceNotFoundException("Note not found with id: n1"));

        mockMvc.perform(put("/notes/update/n1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notes)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Note not found with id: n1"));
    }

    @Test
    void deleteNotes_found_returnsSuccessMessage() throws Exception {
        when(notesService.deleteNotes("n1")).thenReturn("Notes Deleted Successfully");

        mockMvc.perform(delete("/notes/delete/n1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notes Deleted Successfully"));
    }

    @Test
    void deleteNotes_notFound_returns404() throws Exception {
        when(notesService.deleteNotes("n1")).thenThrow(new ResourceNotFoundException("Note not found with id: n1"));

        mockMvc.perform(delete("/notes/delete/n1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Note not found with id: n1"));
    }
}
