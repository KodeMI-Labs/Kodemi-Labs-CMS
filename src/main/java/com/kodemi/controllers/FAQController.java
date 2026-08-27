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

import com.kodemi.dto.FAQDto;
import com.kodemi.model.FAQ;
import com.kodemi.service.FAQService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FAQController {

    private final FAQService faqService;
    @PostMapping("/create-dto")
    public FAQ createFAQDto(
            @RequestBody FAQDto faqDto) {

        return faqService.createFAQ(faqDto);
    }
    @PostMapping("/create")
    public String createFAQ(
            @RequestBody FAQ faq) {

        return faqService.createFAQ(faq);
    }
    @GetMapping("/{faq_id}")
    public FAQDto getFAQById(
            @PathVariable String faq_id) {
        return faqService.getFAQId(faq_id);
    }
    @GetMapping("/all")
    public List<FAQDto> getAllFAQ() {
        return faqService.getAllFAQ();
    }
    @PutMapping("/update/{faq_id}")
    public String updateFAQ(
            @PathVariable String faq_id,
            @RequestBody FAQ faq) {
        return faqService.updateFAQ(
                faq_id,
                faq
        );
    }
    @DeleteMapping("/delete/{faq_id}")
    public String deleteFAQ(
            @PathVariable String faq_id) {
        return faqService.deleteFAQ(faq_id);
    }
}