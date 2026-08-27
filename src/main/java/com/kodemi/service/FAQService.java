package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.FAQDto;
import com.kodemi.model.FAQ;

public interface FAQService {
	FAQ createFAQ(FAQDto faqDto);

	String createFAQ(FAQ faq);

	FAQDto getFAQId(String faq_id);

	List<FAQDto> getAllFAQ();

	String updateFAQ(String faq_id, FAQ faq);

	String deleteFAQ(String faq_id);
}
