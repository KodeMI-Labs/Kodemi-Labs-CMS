package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.FAQDto;
import com.ContentManagementSystem.CMS.model.FAQ;
import com.ContentManagementSystem.CMS.service.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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