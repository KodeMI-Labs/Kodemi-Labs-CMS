package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.TestimonialDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Testimonial;
import com.ContentManagementSystem.CMS.repository.TestimonialRepository;
import com.ContentManagementSystem.CMS.service.TestimonialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TestimonialServiceImpl implements TestimonialService {

    private final TestimonialRepository testimonialRepository;

    public TestimonialServiceImpl(TestimonialRepository testimonialRepository) {
        this.testimonialRepository = testimonialRepository;
    }

    @Override
    public Testimonial createTestimonial(TestimonialDto testimonialDto) {
        Testimonial testimonial = new Testimonial();
        BeanUtils.copyProperties(testimonialDto, testimonial);
        testimonial.setTestimonial_id(UUID.randomUUID().toString());
        testimonial.setCreated_at(LocalDateTime.now().toString());
        testimonial.setUpdated_at(LocalDateTime.now().toString());
        if (testimonial.getStatus() == null) testimonial.setStatus("PENDING");
        if (testimonial.getFeatured() == null) testimonial.setFeatured(false);
        testimonialRepository.save(testimonial);
        return testimonial;
    }

    @Override
    public String createTestimonial(Testimonial testimonial) {
        testimonial.setTestimonial_id(UUID.randomUUID().toString());
        testimonial.setCreated_at(LocalDateTime.now().toString());
        testimonial.setUpdated_at(LocalDateTime.now().toString());
        if (testimonial.getStatus() == null) testimonial.setStatus("PENDING");
        if (testimonial.getFeatured() == null) testimonial.setFeatured(false);
        testimonialRepository.save(testimonial);
        return "Testimonial Created Successfully";
    }

    @Override
    public TestimonialDto getTestimonialById(String testimonial_id) {
        Testimonial testimonial = testimonialRepository.findById(testimonial_id);
        if (testimonial == null) {
            throw new ResourceNotFoundException("Testimonial not found with id: " + testimonial_id);
        }
        TestimonialDto dto = new TestimonialDto();
        BeanUtils.copyProperties(testimonial, dto);
        return dto;
    }

    @Override
    public List<TestimonialDto> getAllTestimonials() {
        List<Testimonial> list = testimonialRepository.findAll();
        List<TestimonialDto> dtoList = new ArrayList<>();
        for (Testimonial testimonial : list) {
            TestimonialDto dto = new TestimonialDto();
            BeanUtils.copyProperties(testimonial, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<TestimonialDto> getFeaturedTestimonials() {
        List<Testimonial> all = testimonialRepository.findAll();
        List<TestimonialDto> dtoList = new ArrayList<>();
        for (Testimonial testimonial : all) {
            if (Boolean.TRUE.equals(testimonial.getFeatured())
                    && "ACTIVE".equals(testimonial.getStatus())) {
                TestimonialDto dto = new TestimonialDto();
                BeanUtils.copyProperties(testimonial, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public List<TestimonialDto> getTestimonialsByStatus(String status) {
        List<Testimonial> all = testimonialRepository.findAll();
        List<TestimonialDto> dtoList = new ArrayList<>();
        for (Testimonial testimonial : all) {
            if (status.equalsIgnoreCase(testimonial.getStatus())) {
                TestimonialDto dto = new TestimonialDto();
                BeanUtils.copyProperties(testimonial, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public String updateTestimonial(String testimonial_id, Testimonial testimonial) {
        Testimonial existing = testimonialRepository.findById(testimonial_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Testimonial not found with id: " + testimonial_id);
        }
        existing.setUser_name(testimonial.getUser_name());
        existing.setUser_profile_image(testimonial.getUser_profile_image());
        existing.setUser_designation(testimonial.getUser_designation());
        existing.setUser_company(testimonial.getUser_company());
        existing.setContent(testimonial.getContent());
        existing.setRating(testimonial.getRating());
        existing.setStatus(testimonial.getStatus());
        existing.setFeatured(testimonial.getFeatured());
        existing.setUpdated_at(LocalDateTime.now().toString());
        testimonialRepository.save(existing);
        return "Testimonial Updated Successfully";
    }

    @Override
    public String delete(String testimonial_id) {
        Testimonial testimonial = testimonialRepository.findById(testimonial_id);
        if (testimonial == null) {
            throw new ResourceNotFoundException("Testimonial not found with id: " + testimonial_id);
        }
        testimonialRepository.delete(testimonial_id);
        return "Testimonial Deleted Successfully";
    }
}
