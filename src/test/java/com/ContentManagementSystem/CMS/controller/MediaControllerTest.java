package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.MediaDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Media;
import com.ContentManagementSystem.CMS.service.MediaService;
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

@WebMvcTest(MediaController.class)
@AutoConfigureMockMvc(addFilters = false)
class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MediaService mediaService;

    @MockBean
    private JwtUtil jwtUtil;

    private Media media;
    private MediaDto mediaDto;

    @BeforeEach
    void setUp() {
        media = new Media();
        media.setMeta_Id("m1");
        media.setFile_name("image.png");
        media.setFile_type("IMAGE");
        media.setFile_url("http://cdn.com/image.png");
        media.setUploaded_by("user1");

        mediaDto = new MediaDto();
        mediaDto.setMeta_Id("m1");
        mediaDto.setFile_name("image.png");
        mediaDto.setFile_type("IMAGE");
        mediaDto.setFile_url("http://cdn.com/image.png");
        mediaDto.setUploaded_by("user1");
    }

    @Test
    void createMedia_returnsSuccessMessage() throws Exception {
        when(mediaService.createMedia(any(Media.class))).thenReturn("Media Created Successfully");

        mockMvc.perform(post("/media/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(media)))
                .andExpect(status().isOk())
                .andExpect(content().string("Media Created Successfully"));
    }

    @Test
    void createMediaDto_returnsMedia() throws Exception {
        when(mediaService.createMedia(any(MediaDto.class))).thenReturn(media);

        mockMvc.perform(post("/media/create-dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mediaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta_Id").value("m1"))
                .andExpect(jsonPath("$.file_name").value("image.png"));
    }

    @Test
    void getMediaById_found_returnsDto() throws Exception {
        when(mediaService.getMediaId("m1")).thenReturn(mediaDto);

        mockMvc.perform(get("/media/m1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta_Id").value("m1"))
                .andExpect(jsonPath("$.file_name").value("image.png"));
    }

    @Test
    void getMediaById_notFound_returns404() throws Exception {
        when(mediaService.getMediaId("m1")).thenThrow(new ResourceNotFoundException("Media not found with id: m1"));

        mockMvc.perform(get("/media/m1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Media not found with id: m1"));
    }

    @Test
    void getAllMedia_returnsList() throws Exception {
        when(mediaService.getAllMedia()).thenReturn(List.of(mediaDto));

        mockMvc.perform(get("/media/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateMedia_found_returnsSuccessMessage() throws Exception {
        when(mediaService.updateMedia(eq("m1"), any(Media.class))).thenReturn("Media Updated Successfully");

        mockMvc.perform(put("/media/update/m1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(media)))
                .andExpect(status().isOk())
                .andExpect(content().string("Media Updated Successfully"));
    }

    @Test
    void updateMedia_notFound_returns404() throws Exception {
        when(mediaService.updateMedia(eq("m1"), any(Media.class)))
                .thenThrow(new ResourceNotFoundException("Media not found with id: m1"));

        mockMvc.perform(put("/media/update/m1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(media)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Media not found with id: m1"));
    }

    @Test
    void deleteMedia_found_returnsSuccessMessage() throws Exception {
        when(mediaService.deleteMedia("m1")).thenReturn("Media Deleted Successfully");

        mockMvc.perform(delete("/media/delete/m1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Media Deleted Successfully"));
    }

    @Test
    void deleteMedia_notFound_returns404() throws Exception {
        when(mediaService.deleteMedia("m1")).thenThrow(new ResourceNotFoundException("Media not found with id: m1"));

        mockMvc.perform(delete("/media/delete/m1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Media not found with id: m1"));
    }
}
