package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.StaticPagesDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.StaticPages;
import com.ContentManagementSystem.CMS.service.StaticPagesService;
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

@WebMvcTest(StaticPagesController.class)
@AutoConfigureMockMvc(addFilters = false)
class StaticPagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StaticPagesService staticPagesService;

    @MockBean
    private JwtUtil jwtUtil;

    private StaticPages staticPages;
    private StaticPagesDto staticPagesDto;

    @BeforeEach
    void setUp() {
        staticPages = new StaticPages();
        staticPages.setPage_id("p1");
        staticPages.setTitle("About Us");
        staticPages.setSlug("about-us");
        staticPages.setContent("About us content");
        staticPages.setStatus("PUBLISHED");

        staticPagesDto = new StaticPagesDto();
        staticPagesDto.setPage_Id("p1");
        staticPagesDto.setTitle("About Us");
        staticPagesDto.setSlug("about-us");
        staticPagesDto.setContent("About us content");
        staticPagesDto.setStatus("PUBLISHED");
    }

    @Test
    void createStaticPages_returnsSuccessMessage() throws Exception {
        when(staticPagesService.createStaticPages(any(StaticPages.class))).thenReturn("Static Page Created Successfully");

        mockMvc.perform(post("/static-pages/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staticPages)))
                .andExpect(status().isOk())
                .andExpect(content().string("Static Page Created Successfully"));
    }

    @Test
    void createStaticPagesDto_returnsStaticPages() throws Exception {
        when(staticPagesService.createStaticPages(any(StaticPagesDto.class))).thenReturn(staticPages);

        mockMvc.perform(post("/static-pages/create-dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staticPagesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page_id").value("p1"))
                .andExpect(jsonPath("$.title").value("About Us"));
    }

    @Test
    void getStaticPageById_found_returnsDto() throws Exception {
        when(staticPagesService.getAllStaticPages("p1")).thenReturn(staticPagesDto);

        mockMvc.perform(get("/static-pages/p1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page_Id").value("p1"))
                .andExpect(jsonPath("$.title").value("About Us"));
    }

    @Test
    void getStaticPageById_notFound_returns404() throws Exception {
        when(staticPagesService.getAllStaticPages("p1"))
                .thenThrow(new ResourceNotFoundException("Static page not found with id: p1"));

        mockMvc.perform(get("/static-pages/p1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Static page not found with id: p1"));
    }

    @Test
    void getAllStaticPages_returnsList() throws Exception {
        when(staticPagesService.getAllStaticPages()).thenReturn(List.of(staticPagesDto));

        mockMvc.perform(get("/static-pages/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateStaticPages_found_returnsSuccessMessage() throws Exception {
        when(staticPagesService.updateStaticPages(eq("p1"), any(StaticPages.class))).thenReturn("Static Page Updated Successfully");

        mockMvc.perform(put("/static-pages/update/p1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staticPages)))
                .andExpect(status().isOk())
                .andExpect(content().string("Static Page Updated Successfully"));
    }

    @Test
    void updateStaticPages_notFound_returns404() throws Exception {
        when(staticPagesService.updateStaticPages(eq("p1"), any(StaticPages.class)))
                .thenThrow(new ResourceNotFoundException("Static page not found with id: p1"));

        mockMvc.perform(put("/static-pages/update/p1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staticPages)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Static page not found with id: p1"));
    }

    @Test
    void deleteStaticPages_found_returnsSuccessMessage() throws Exception {
        when(staticPagesService.deleteStaticPages("p1")).thenReturn("Static Page Deleted Successfully");

        mockMvc.perform(delete("/static-pages/delete/p1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Static Page Deleted Successfully"));
    }

    @Test
    void deleteStaticPages_notFound_returns404() throws Exception {
        when(staticPagesService.deleteStaticPages("p1"))
                .thenThrow(new ResourceNotFoundException("Static page not found with id: p1"));

        mockMvc.perform(delete("/static-pages/delete/p1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Static page not found with id: p1"));
    }
}
