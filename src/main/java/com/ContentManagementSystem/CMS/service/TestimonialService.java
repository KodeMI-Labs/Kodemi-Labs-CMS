package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.TestimonialDto;
import com.ContentManagementSystem.CMS.model.Testimonial;

import java.util.List;

public interface TestimonialService {
    Testimonial createTestimonial(TestimonialDto testimonialDto);
    String createTestimonial(Testimonial testimonial);
    TestimonialDto getTestimonialById(String testimonial_id);
    List<TestimonialDto> getAllTestimonials();
    List<TestimonialDto> getFeaturedTestimonials();
    List<TestimonialDto> getTestimonialsByStatus(String status);
    String updateTestimonial(String testimonial_id, Testimonial testimonial);
    String delete(String testimonial_id);
}
