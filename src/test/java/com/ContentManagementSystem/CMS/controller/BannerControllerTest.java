package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.BannerDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Banner;
import com.ContentManagementSystem.CMS.service.BannerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.ContentManagementSystem.CMS.config.JwtUtil;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BannerController.class)
@AutoConfigureMockMvc(addFilters = false)
class BannerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BannerService bannerService;

    @MockBean
    private JwtUtil jwtUtil;

    private Banner banner;
    private BannerDto bannerDto;

    @BeforeEach
    void setUp() {
        banner = new Banner();
        banner.setBanner_Id("b1");
        banner.setTitle("Test Banner");
        banner.setStatus("ACTIVE");

        bannerDto = new BannerDto();
        bannerDto.setBanner_Id("b1");
        bannerDto.setTitle("Test Banner");
        bannerDto.setStatus("ACTIVE");
    }

    @Test
    void createBanner_returnsSuccessMessage() throws Exception {
        when(bannerService.createBanner(any(Banner.class))).thenReturn("Banner Created Successfully");

        mockMvc.perform(post("/banner/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(banner)))
                .andExpect(status().isOk())
                .andExpect(content().string("Banner Created Successfully"));
    }

    @Test
    void createBannerDto_returnsBanner() throws Exception {
        when(bannerService.createBanner(any(BannerDto.class))).thenReturn(banner);

        mockMvc.perform(post("/banner/create-dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bannerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.banner_Id").value("b1"))
                .andExpect(jsonPath("$.title").value("Test Banner"));
    }

    @Test
    void getBannerById_found_returnsDto() throws Exception {
        when(bannerService.getBannerId("b1")).thenReturn(bannerDto);

        mockMvc.perform(get("/banner/b1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.banner_Id").value("b1"))
                .andExpect(jsonPath("$.title").value("Test Banner"));
    }

    @Test
    void getBannerById_notFound_returns404() throws Exception {
        when(bannerService.getBannerId("b1")).thenThrow(new ResourceNotFoundException("Banner not found with id: b1"));

        mockMvc.perform(get("/banner/b1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Banner not found with id: b1"));
    }

    @Test
    void getAllBanners_returnsList() throws Exception {
        when(bannerService.getAllBanner()).thenReturn(List.of(bannerDto));

        mockMvc.perform(get("/banner/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Banner"));
    }

    @Test
    void updateBanner_found_returnsSuccessMessage() throws Exception {
        when(bannerService.updateBanner(eq("b1"), any(Banner.class))).thenReturn("Banner Updated Successfully");

        mockMvc.perform(put("/banner/update/b1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(banner)))
                .andExpect(status().isOk())
                .andExpect(content().string("Banner Updated Successfully"));
    }

    @Test
    void updateBanner_notFound_returns404() throws Exception {
        when(bannerService.updateBanner(eq("b1"), any(Banner.class)))
                .thenThrow(new ResourceNotFoundException("Banner not found with id: b1"));

        mockMvc.perform(put("/banner/update/b1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(banner)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Banner not found with id: b1"));
    }

    @Test
    void deleteBanner_found_returnsSuccessMessage() throws Exception {
        when(bannerService.deleteBanner("b1")).thenReturn("Banner Deleted Successfully");

        mockMvc.perform(delete("/banner/delete/b1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Banner Deleted Successfully"));
    }

    @Test
    void deleteBanner_notFound_returns404() throws Exception {
        when(bannerService.deleteBanner("b1")).thenThrow(new ResourceNotFoundException("Banner not found with id: b1"));

        mockMvc.perform(delete("/banner/delete/b1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Banner not found with id: b1"));
    }
}
