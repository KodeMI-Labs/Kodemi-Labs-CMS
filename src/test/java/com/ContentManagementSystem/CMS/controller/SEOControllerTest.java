package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.SEODto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.SEO;
import com.ContentManagementSystem.CMS.service.SEOService;
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

@WebMvcTest(SEOController.class)
@AutoConfigureMockMvc(addFilters = false)
class SEOControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SEOService seoService;

    @MockBean
    private JwtUtil jwtUtil;

    private SEO seo;
    private SEODto seoDto;

    @BeforeEach
    void setUp() {
        seo = new SEO();
        seo.setSeo_id("seo1");
        seo.setMeta_title("Home Page");
        seo.setMeta_description("Welcome");
        seo.setCanonical_url("http://example.com");
        seo.setRobots_tag("INDEX_FOLLOW");

        seoDto = new SEODto();
        seoDto.setSeo_id("seo1");
        seoDto.setMeta_title("Home Page");
        seoDto.setMeta_description("Welcome");
        seoDto.setCanonical_url("http://example.com");
        seoDto.setRobots_tag("INDEX_FOLLOW");
    }

    @Test
    void createSEO_returnsSuccessMessage() throws Exception {
        when(seoService.createSEO(any(SEO.class))).thenReturn("SEO Created Successfully");

        mockMvc.perform(post("/seo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seo)))
                .andExpect(status().isOk())
                .andExpect(content().string("SEO Created Successfully"));
    }

    @Test
    void createSEODto_returnsSEO() throws Exception {
        when(seoService.createSEO(any(SEODto.class))).thenReturn(seo);

        mockMvc.perform(post("/seo/create-dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seo_id").value("seo1"))
                .andExpect(jsonPath("$.meta_title").value("Home Page"));
    }

    @Test
    void getSEOById_found_returnsDto() throws Exception {
        when(seoService.getSEOId("seo1")).thenReturn(seoDto);

        mockMvc.perform(get("/seo/seo1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seo_id").value("seo1"))
                .andExpect(jsonPath("$.meta_title").value("Home Page"));
    }

    @Test
    void getSEOById_notFound_returns404() throws Exception {
        when(seoService.getSEOId("seo1")).thenThrow(new ResourceNotFoundException("SEO not found with id: seo1"));

        mockMvc.perform(get("/seo/seo1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SEO not found with id: seo1"));
    }

    @Test
    void getAllSEO_returnsList() throws Exception {
        when(seoService.getAllSeo()).thenReturn(List.of(seoDto));

        mockMvc.perform(get("/seo/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateSEO_found_returnsSuccessMessage() throws Exception {
        when(seoService.update(eq("seo1"), any(SEO.class))).thenReturn("SEO Updated Successfully");

        mockMvc.perform(put("/seo/update/seo1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seo)))
                .andExpect(status().isOk())
                .andExpect(content().string("SEO Updated Successfully"));
    }

    @Test
    void updateSEO_notFound_returns404() throws Exception {
        when(seoService.update(eq("seo1"), any(SEO.class)))
                .thenThrow(new ResourceNotFoundException("SEO not found with id: seo1"));

        mockMvc.perform(put("/seo/update/seo1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seo)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SEO not found with id: seo1"));
    }

    @Test
    void deleteSEO_found_returnsSuccessMessage() throws Exception {
        when(seoService.deleteSEO("seo1")).thenReturn("SEO Deleted Successfully");

        mockMvc.perform(delete("/seo/delete/seo1"))
                .andExpect(status().isOk())
                .andExpect(content().string("SEO Deleted Successfully"));
    }

    @Test
    void deleteSEO_notFound_returns404() throws Exception {
        when(seoService.deleteSEO("seo1")).thenThrow(new ResourceNotFoundException("SEO not found with id: seo1"));

        mockMvc.perform(delete("/seo/delete/seo1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SEO not found with id: seo1"));
    }
}
