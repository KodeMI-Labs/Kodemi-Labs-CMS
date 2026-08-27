package com.kodemi.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.ContactUsDto;
import com.kodemi.model.ContactUs;
import com.kodemi.repository.ContactUsRepository;
import com.kodemi.service.ContactUsService;

@Service
public class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepository contactUsRepository;

    public ContactUsServiceImpl(ContactUsRepository contactUsRepository) {
        this.contactUsRepository = contactUsRepository;
    }

    @Override
    public ContactUs createContactUs(ContactUsDto contactUsDto) {
        ContactUs contactUs = new ContactUs();
        BeanUtils.copyProperties(contactUsDto, contactUs);
        contactUs.setContact_id(UUID.randomUUID().toString());
        contactUs.setStatus("UNREAD");  // default status
        contactUs.setCreated_at(LocalDateTime.now().toString());
        contactUs.setUpdated_at(LocalDateTime.now().toString());
        contactUsRepository.save(contactUs);
        return contactUs;
    }

    @Override
    public String createContactUs(ContactUs contactUs) {
        contactUs.setContact_id(UUID.randomUUID().toString());
        if (contactUs.getStatus() == null) {
            contactUs.setStatus("UNREAD");
        }
        contactUs.setCreated_at(LocalDateTime.now().toString());
        contactUs.setUpdated_at(LocalDateTime.now().toString());
        contactUsRepository.save(contactUs);
        return "Contact request submitted successfully";
    }

    @Override
    public ContactUsDto getContactUsById(String contact_id) {
        ContactUs contactUs = contactUsRepository.findById(contact_id);
        if (contactUs == null) {
            throw new ResourceNotFoundException("Contact request not found with id: " + contact_id);
        }
        ContactUsDto dto = new ContactUsDto();
        BeanUtils.copyProperties(contactUs, dto);
        return dto;
    }

    @Override
    public List<ContactUsDto> getAllContactUs() {
        List<ContactUs> list = contactUsRepository.findAll();
        List<ContactUsDto> dtoList = new ArrayList<>();
        for (ContactUs contactUs : list) {
            ContactUsDto dto = new ContactUsDto();
            BeanUtils.copyProperties(contactUs, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<ContactUsDto> getContactUsByStatus(String status) {
        List<ContactUs> all = contactUsRepository.findAll();
        List<ContactUsDto> dtoList = new ArrayList<>();
        for (ContactUs contactUs : all) {
            if (status.equalsIgnoreCase(contactUs.getStatus())) {
                ContactUsDto dto = new ContactUsDto();
                BeanUtils.copyProperties(contactUs, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public String updateContactUs(String contact_id, ContactUs contactUs) {
        ContactUs existing = contactUsRepository.findById(contact_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Contact request not found with id: " + contact_id);
        }
        existing.setStatus(contactUs.getStatus());
        existing.setUpdated_at(LocalDateTime.now().toString());
        contactUsRepository.save(existing);
        return "Contact request updated successfully";
    }

    @Override
    public String deleteContactUs(String contact_id) {
        ContactUs contactUs = contactUsRepository.findById(contact_id);
        if (contactUs == null) {
            throw new ResourceNotFoundException("Contact request not found with id: " + contact_id);
        }
        contactUsRepository.delete(contact_id);
        return "Contact request deleted successfully";
    }
}
