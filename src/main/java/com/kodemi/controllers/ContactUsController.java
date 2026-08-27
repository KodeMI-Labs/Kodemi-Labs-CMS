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

import com.kodemi.dto.ContactUsDto;
import com.kodemi.model.ContactUs;
import com.kodemi.service.ContactUsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contact-us")
@RequiredArgsConstructor
public class ContactUsController {

    private final ContactUsService contactUsService;

    /**
     * POST /contact-us/create
     * Submit a contact form — public endpoint.
     */
    @PostMapping("/create")
    public String createContactUs(@RequestBody ContactUs contactUs) {
        return contactUsService.createContactUs(contactUs);
    }

    /**
     * POST /contact-us/create-dto
     * Submit contact form — returns full object.
     */
    @PostMapping("/create-dto")
    public ContactUs createContactUsDto(@RequestBody ContactUsDto contactUsDto) {
        return contactUsService.createContactUs(contactUsDto);
    }

    /**
     * GET /contact-us/{contact_id}
     * Admin: view single contact request.
     */
    @GetMapping("/{contact_id}")
    public ContactUsDto getContactUsById(@PathVariable String contact_id) {
        return contactUsService.getContactUsById(contact_id);
    }

    /**
     * GET /contact-us/all
     * Admin: view all contact requests.
     */
    @GetMapping("/all")
    public List<ContactUsDto> getAllContactUs() {
        return contactUsService.getAllContactUs();
    }

    /**
     * GET /contact-us/status/{status}
     * Admin: filter by status (UNREAD, READ, RESOLVED).
     */
    @GetMapping("/status/{status}")
    public List<ContactUsDto> getContactUsByStatus(@PathVariable String status) {
        return contactUsService.getContactUsByStatus(status);
    }

    /**
     * PUT /contact-us/update/{contact_id}
     * Admin: update status (mark as READ or RESOLVED).
     */
    @PutMapping("/update/{contact_id}")
    public String updateContactUs(@PathVariable String contact_id,
                                  @RequestBody ContactUs contactUs) {
        return contactUsService.updateContactUs(contact_id, contactUs);
    }

    /**
     * DELETE /contact-us/delete/{contact_id}
     * Admin: delete a contact request.
     */
    @DeleteMapping("/delete/{contact_id}")
    public String deleteContactUs(@PathVariable String contact_id) {
        return contactUsService.deleteContactUs(contact_id);
    }
}
