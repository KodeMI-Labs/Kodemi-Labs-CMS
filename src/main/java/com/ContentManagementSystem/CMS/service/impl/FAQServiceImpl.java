package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.FAQDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.FAQ;
import com.ContentManagementSystem.CMS.repository.FAQRepository;
import com.ContentManagementSystem.CMS.service.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {
    private final FAQRepository faqRepository;
    @Override
    public FAQ createFAQ(FAQDto faqDto) {
        FAQ faq = new FAQ();
        BeanUtils.copyProperties(faqDto, faq);
        faq.setFaq_id(UUID.randomUUID().toString());
        faq.setCreated_at(LocalDateTime.now());
        faq.setUpdated_at(LocalDateTime.now());
        faqRepository.save(faq);
        return faq;
    }
    @Override
    public String createFAQ(FAQ faq) {
        faq.setFaq_id(UUID.randomUUID().toString());
        faq.setCreated_at(LocalDateTime.now());
        faq.setUpdated_at(LocalDateTime.now());
        faqRepository.save(faq);
        return "FAQ Created Successfully";
    }
    @Override
    public FAQDto getFAQId(String faq_id) {
        FAQ faq = faqRepository.findById(faq_id);
        if (faq == null) {
            throw new ResourceNotFoundException("FAQ not found with id: " + faq_id);
        }
        FAQDto faqDto = new FAQDto();
        BeanUtils.copyProperties(faq, faqDto);
        return faqDto;
    }
    @Override
    public List<FAQDto> getAllFAQ() {
        List<FAQ> faqList = faqRepository.findAll();
        List<FAQDto> faqDtoList = new ArrayList<>();
        for (FAQ faq : faqList) {
            FAQDto faqDto = new FAQDto();
            BeanUtils.copyProperties(faq, faqDto);
            faqDtoList.add(faqDto);
        }
        return faqDtoList;
    }
    @Override
    public String updateFAQ(String faq_id, FAQ faq) {
        FAQ existingFAQ = faqRepository.findById(faq_id);
        if (existingFAQ == null) {
            throw new ResourceNotFoundException("FAQ not found with id: " + faq_id);
        }
        existingFAQ.setQuestion(faq.getQuestion());
        existingFAQ.setAnswer(faq.getAnswer());
        existingFAQ.setCategory(faq.getCategory());
        existingFAQ.setStatus(faq.getStatus());
        existingFAQ.setUpdated_at(LocalDateTime.now());
        faqRepository.save(existingFAQ);
        return "FAQ Updated Successfully";
    }
    @Override
    public String deleteFAQ(String faq_id) {
        FAQ faq = faqRepository.findById(faq_id);
        if (faq == null) {
            throw new ResourceNotFoundException("FAQ not found with id: " + faq_id);
        }
        faqRepository.delete(faq_id);
        return "FAQ Deleted Successfully";
    }
}