package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.RatingSummaryResponse;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorRatingSummary;
import com.ContentManagementSystem.CMS.repository.reviewsandratingrepository.InstructorRatingSummaryRepository;
import com.ContentManagementSystem.CMS.service.InstructorRatingSummaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorRatingSummaryServiceImpl implements InstructorRatingSummaryService {

    private final InstructorRatingSummaryRepository instructorRatingSummaryRepository;

    public InstructorRatingSummaryServiceImpl(InstructorRatingSummaryRepository instructorRatingSummaryRepository) {
        this.instructorRatingSummaryRepository = instructorRatingSummaryRepository;
    }

    @Override
    public InstructorRatingSummary createInstructorRatingSummary(String instructor_id, RatingSummaryResponse ratingSummaryResponse) {
        InstructorRatingSummary summary = new InstructorRatingSummary();
        BeanUtils.copyProperties(ratingSummaryResponse, summary);
        summary.setInstructor_id(instructor_id);
        instructorRatingSummaryRepository.save(summary);
        return summary;
    }

    @Override
    public String createInstructorRatingSummary(InstructorRatingSummary instructorRatingSummary) {
        instructorRatingSummaryRepository.save(instructorRatingSummary);
        return "Instructor Rating Summary Created Successfully";
    }

    @Override
    public RatingSummaryResponse getInstructorRatingSummaryById(String instructor_id) {
        InstructorRatingSummary summary = instructorRatingSummaryRepository.findById(instructor_id);
        if (summary == null) {
            throw new ResourceNotFoundException("Instructor Rating Summary not found for instructor_id: " + instructor_id);
        }
        return mapToResponse(summary);
    }

    @Override
    public List<RatingSummaryResponse> getAllInstructorRatingSummaries() {
        List<InstructorRatingSummary> summaries = instructorRatingSummaryRepository.findAll();
        List<RatingSummaryResponse> responses = new ArrayList<>();
        for (InstructorRatingSummary summary : summaries) {
            responses.add(mapToResponse(summary));
        }
        return responses;
    }

    @Override
    public String updateInstructorRatingSummary(String instructor_id, InstructorRatingSummary instructorRatingSummary) {
        InstructorRatingSummary existing = instructorRatingSummaryRepository.findById(instructor_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Instructor Rating Summary not found for instructor_id: " + instructor_id);
        }
        existing.setAverage_rating(instructorRatingSummary.getAverage_rating());
        existing.setTotal_reviews(instructorRatingSummary.getTotal_reviews());
        existing.setFive_star_count(instructorRatingSummary.getFive_star_count());
        existing.setFour_star_count(instructorRatingSummary.getFour_star_count());
        existing.setThree_star_count(instructorRatingSummary.getThree_star_count());
        existing.setTwoStarCount(instructorRatingSummary.getTwoStarCount());
        existing.setOne_star_count(instructorRatingSummary.getOne_star_count());
        instructorRatingSummaryRepository.save(existing);
        return "Instructor Rating Summary Updated Successfully";
    }

    @Override
    public String delete(String instructor_id) {
        InstructorRatingSummary summary = instructorRatingSummaryRepository.findById(instructor_id);
        if (summary == null) {
            throw new ResourceNotFoundException("Instructor Rating Summary not found for instructor_id: " + instructor_id);
        }
        instructorRatingSummaryRepository.delete(instructor_id);
        return "Instructor Rating Summary Deleted Successfully";
    }

    private RatingSummaryResponse mapToResponse(InstructorRatingSummary summary) {
        RatingSummaryResponse response = new RatingSummaryResponse();
        BeanUtils.copyProperties(summary, response);
        return response;
    }
}
