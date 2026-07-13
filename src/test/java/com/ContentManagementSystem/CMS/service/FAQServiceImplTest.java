package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.FAQDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.FAQ;
import com.ContentManagementSystem.CMS.repository.FAQRepository;
import com.ContentManagementSystem.CMS.service.impl.FAQServiceImpl;
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
class FAQServiceImplTest {

    @Mock
    private FAQRepository faqRepository;

    @InjectMocks
    private FAQServiceImpl faqService;

    private FAQ faq;

    @BeforeEach
    void setUp() {
        faq = new FAQ();
        faq.setFaq_id("faq1");
        faq.setQuestion("What is CMS?");
        faq.setAnswer("Content Management System");
        faq.setStatus("ACTIVE");
    }

    @Test
    void createFAQ_withDto_returnsFAQ() {
        FAQDto dto = new FAQDto();
        dto.setFaq_Id("faq1");
        dto.setQuestion("What is CMS?");
        when(faqRepository.save(any(FAQ.class))).thenAnswer(i -> i.getArgument(0));

        FAQ result = faqService.createFAQ(dto);

        assertNotNull(result);
        assertEquals("What is CMS?", result.getQuestion());
    }

    @Test
    void createFAQ_withModel_returnsSuccessMessage() {
        when(faqRepository.save(faq)).thenReturn(faq);

        String result = faqService.createFAQ(faq);

        assertEquals("FAQ Created Successfully", result);
    }

    @Test
    void getFAQId_found_returnsDto() {
        when(faqRepository.findById("faq1")).thenReturn(faq);

        FAQDto result = faqService.getFAQId("faq1");

        assertNotNull(result);
        assertEquals("What is CMS?", result.getQuestion());
    }

    @Test
    void getFAQId_notFound_throwsException() {
        when(faqRepository.findById("faq1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> faqService.getFAQId("faq1"));
    }

    @Test
    void getAllFAQ_returnsList() {
        when(faqRepository.findAll()).thenReturn(List.of(faq));

        List<FAQDto> result = faqService.getAllFAQ();

        assertEquals(1, result.size());
    }

    @Test
    void updateFAQ_found_returnsSuccessMessage() {
        when(faqRepository.findById("faq1")).thenReturn(faq);
        when(faqRepository.save(any())).thenReturn(faq);

        String result = faqService.updateFAQ("faq1", faq);

        assertEquals("FAQ Updated Successfully", result);
    }

    @Test
    void updateFAQ_notFound_throwsException() {
        when(faqRepository.findById("faq1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> faqService.updateFAQ("faq1", faq));
    }

    @Test
    void deleteFAQ_found_returnsSuccessMessage() {
        when(faqRepository.findById("faq1")).thenReturn(faq);

        String result = faqService.deleteFAQ("faq1");

        assertEquals("FAQ Deleted Successfully", result);
        verify(faqRepository).delete("faq1");
    }

    @Test
    void deleteFAQ_notFound_throwsException() {
        when(faqRepository.findById("faq1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> faqService.deleteFAQ("faq1"));
    }
}
