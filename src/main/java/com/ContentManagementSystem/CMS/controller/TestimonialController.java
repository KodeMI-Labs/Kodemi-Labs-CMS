package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.TestimonialDto;
import com.ContentManagementSystem.CMS.model.Testimonial;
import com.ContentManagementSystem.CMS.service.TestimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testimonial")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;

    /**
     * POST /testimonial/create
     * Creates a testimonial — returns success message.
     */
    @PostMapping("/create")
    public String createTestimonial(@RequestBody Testimonial testimonial) {
        return testimonialService.createTestimonial(testimonial);
    }

    /**
     * POST /testimonial/create-dto
     * Creates a testimonial — returns the saved object.
     */
    @PostMapping("/create-dto")
    public Testimonial createTestimonialDto(@RequestBody TestimonialDto testimonialDto) {
        return testimonialService.createTestimonial(testimonialDto);
    }

    /**
     * GET /testimonial/{testimonial_id}
     * Get a single testimonial by ID.
     */
    @GetMapping("/{testimonial_id}")
    public TestimonialDto getTestimonialById(@PathVariable String testimonial_id) {
        return testimonialService.getTestimonialById(testimonial_id);
    }

    /**
     * GET /testimonial/all
     * Get all testimonials.
     */
    @GetMapping("/all")
    public List<TestimonialDto> getAllTestimonials() {
        return testimonialService.getAllTestimonials();
    }

    /**
     * GET /testimonial/featured
     * Get all featured + ACTIVE testimonials — used on public pages.
     */
    @GetMapping("/featured")
    public List<TestimonialDto> getFeaturedTestimonials() {
        return testimonialService.getFeaturedTestimonials();
    }

    /**
     * GET /testimonial/status/{status}
     * Filter testimonials by status — ACTIVE, INACTIVE, PENDING.
     */
    @GetMapping("/status/{status}")
    public List<TestimonialDto> getTestimonialsByStatus(@PathVariable String status) {
        return testimonialService.getTestimonialsByStatus(status);
    }

    /**
     * PUT /testimonial/update/{testimonial_id}
     * Update an existing testimonial.
     */
    @PutMapping("/update/{testimonial_id}")
    public String updateTestimonial(@PathVariable String testimonial_id,
                                    @RequestBody Testimonial testimonial) {
        return testimonialService.updateTestimonial(testimonial_id, testimonial);
    }

    /**
     * DELETE /testimonial/delete/{testimonial_id}
     * Delete a testimonial.
     */
    @DeleteMapping("/delete/{testimonial_id}")
    public String deleteTestimonial(@PathVariable String testimonial_id) {
        return testimonialService.delete(testimonial_id);
    }
}
