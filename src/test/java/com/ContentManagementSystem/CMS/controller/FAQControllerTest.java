package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.FAQDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.FAQ;
import com.ContentManagementSystem.CMS.service.FAQService;
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

@WebMvcTest(FAQController.class)
@AutoConfigureMockMvc(addFilters = false)
class FAQControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FAQService faqService;

    @MockBean
    private JwtUtil jwtUtil;

    private FAQ faq;
    private FAQDto faqDto;

    @BeforeEach
    void setUp() {
        faq = new FAQ();
        faq.setFaq_id("faq1");
        faq.setQuestion("What is CMS?");
        faq.setAnswer("Content Management System");
        faq.setStatus("ACTIVE");

        faqDto = new FAQDto();
        faqDto.setFaq_Id("faq1");
        faqDto.setQuestion("What is CMS?");
        faqDto.setAnswer("Content Management System");
        faqDto.setStatus("ACTIVE");
    }

    @Test
    void createFAQ_returnsSuccessMessage() throws Exception {
        when(faqService.createFAQ(any(FAQ.class))).thenReturn("FAQ Created Successfully");

        mockMvc.perform(post("/faq/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faq)))
                .andExpect(status().isOk())
                .andExpect(content().string("FAQ Created Successfully"));
    }

    @Test
    void createFAQDto_returnsFAQ() throws Exception {
        when(faqService.createFAQ(any(FAQDto.class))).thenReturn(faq);

        mockMvc.perform(post("/faq/create-dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.faq_id").value("faq1"))
                .andExpect(jsonPath("$.question").value("What is CMS?"));
    }

    @Test
    void getFAQById_found_returnsDto() throws Exception {
        when(faqService.getFAQId("faq1")).thenReturn(faqDto);

        mockMvc.perform(get("/faq/faq1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("What is CMS?"));
    }

    @Test
    void getFAQById_notFound_returns404() throws Exception {
        when(faqService.getFAQId("faq1")).thenThrow(new ResourceNotFoundException("FAQ not found with id: faq1"));

        mockMvc.perform(get("/faq/faq1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("FAQ not found with id: faq1"));
    }

    @Test
    void getAllFAQs_returnsList() throws Exception {
        when(faqService.getAllFAQ()).thenReturn(List.of(faqDto));

        mockMvc.perform(get("/faq/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateFAQ_found_returnsSuccessMessage() throws Exception {
        when(faqService.updateFAQ(eq("faq1"), any(FAQ.class))).thenReturn("FAQ Updated Successfully");

        mockMvc.perform(put("/faq/update/faq1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faq)))
                .andExpect(status().isOk())
                .andExpect(content().string("FAQ Updated Successfully"));
    }

    @Test
    void updateFAQ_notFound_returns404() throws Exception {
        when(faqService.updateFAQ(eq("faq1"), any(FAQ.class)))
                .thenThrow(new ResourceNotFoundException("FAQ not found with id: faq1"));

        mockMvc.perform(put("/faq/update/faq1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("FAQ not found with id: faq1"));
    }

    @Test
    void deleteFAQ_found_returnsSuccessMessage() throws Exception {
        when(faqService.deleteFAQ("faq1")).thenReturn("FAQ Deleted Successfully");

        mockMvc.perform(delete("/faq/delete/faq1"))
                .andExpect(status().isOk())
                .andExpect(content().string("FAQ Deleted Successfully"));
    }

    @Test
    void deleteFAQ_notFound_returns404() throws Exception {
        when(faqService.deleteFAQ("faq1")).thenThrow(new ResourceNotFoundException("FAQ not found with id: faq1"));

        mockMvc.perform(delete("/faq/delete/faq1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("FAQ not found with id: faq1"));
    }
}
