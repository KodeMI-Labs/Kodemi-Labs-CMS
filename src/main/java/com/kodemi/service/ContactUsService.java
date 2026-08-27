package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.ContactUsDto;
import com.kodemi.model.ContactUs;

public interface ContactUsService {

	ContactUs createContactUs(ContactUsDto contactUsDto);

	String createContactUs(ContactUs contactUs);

	ContactUsDto getContactUsById(String contact_id);

	List<ContactUsDto> getAllContactUs();

	List<ContactUsDto> getContactUsByStatus(String status);

	String updateContactUs(String contact_id, ContactUs contactUs);

	String deleteContactUs(String contact_id);
}
