package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.FAQDto;
import com.ContentManagementSystem.CMS.model.FAQ;

import java.util.List;

public interface FAQService {
    FAQ createFAQ(FAQDto faqDto);
    String createFAQ(FAQ faq);
    FAQDto getFAQId(String faq_id);
    List<FAQDto> getAllFAQ();
    String updateFAQ(String faq_id,FAQ faq);
    String deleteFAQ(String faq_id);
}
