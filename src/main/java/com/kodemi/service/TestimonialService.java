package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.TestimonialDto;
import com.kodemi.model.Testimonial;

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
