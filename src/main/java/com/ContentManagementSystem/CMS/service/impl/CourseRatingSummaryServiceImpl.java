package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.RatingSummaryResponse;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.reviewsandratings.CourseRatingSummary;
import com.ContentManagementSystem.CMS.repository.reviewsandratingrepository.CourseRatingSummaryRepository;
import com.ContentManagementSystem.CMS.service.CourseRatingSummaryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseRatingSummaryServiceImpl implements CourseRatingSummaryService {

    private final CourseRatingSummaryRepository courseRatingSummaryRepository;

    public CourseRatingSummaryServiceImpl(CourseRatingSummaryRepository courseRatingSummaryRepository) {
        this.courseRatingSummaryRepository = courseRatingSummaryRepository;
    }

    @Override
    public CourseRatingSummary createCourseRatingSummary(String course_id, RatingSummaryResponse ratingSummaryResponse) {
        CourseRatingSummary summary = new CourseRatingSummary();
        summary.setCourse_id(course_id);
        summary.setAverage_rating(ratingSummaryResponse.getAverage_rating());
        summary.setTotal_reviews(ratingSummaryResponse.getTotal_reviews());
        summary.setFive_star_count(ratingSummaryResponse.getFive_star_count());
        summary.setFour_star_count(ratingSummaryResponse.getFour_star_count());
        summary.setThree_star_count(ratingSummaryResponse.getThree_star_count());
        summary.setTwo_star_count(ratingSummaryResponse.getTwo_star_count());
        summary.setOne_star_count(ratingSummaryResponse.getOne_star_count());
        courseRatingSummaryRepository.save(summary);
        return summary;
    }

    @Override
    public String createCourseRatingSummary(CourseRatingSummary courseRatingSummary) {
        courseRatingSummaryRepository.save(courseRatingSummary);
        return "Course Rating Summary Created Successfully";
    }

    @Override
    public RatingSummaryResponse getRatingSummaryId(String course_id) {
        CourseRatingSummary summary = courseRatingSummaryRepository.findById(course_id);
        if (summary == null) {
            throw new ResourceNotFoundException("Course Rating Summary not found for course_id: " + course_id);
        }
        return mapToResponse(summary);
    }

    @Override
    public List<RatingSummaryResponse> getAllCourseRatingSummary() {
        List<CourseRatingSummary> summaries = courseRatingSummaryRepository.findAll();
        List<RatingSummaryResponse> responses = new ArrayList<>();
        for (CourseRatingSummary summary : summaries) {
            responses.add(mapToResponse(summary));
        }
        return responses;
    }

    @Override
    public String updateCourseRatingSummary(String course_id, CourseRatingSummary courseRatingSummary) {
        CourseRatingSummary existing = courseRatingSummaryRepository.findById(course_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Course Rating Summary not found for course_id: " + course_id);
        }
        existing.setAverage_rating(courseRatingSummary.getAverage_rating());
        existing.setTotal_reviews(courseRatingSummary.getTotal_reviews());
        existing.setFive_star_count(courseRatingSummary.getFive_star_count());
        existing.setFour_star_count(courseRatingSummary.getFour_star_count());
        existing.setThree_star_count(courseRatingSummary.getThree_star_count());
        existing.setTwo_star_count(courseRatingSummary.getTwo_star_count());
        existing.setOne_star_count(courseRatingSummary.getOne_star_count());
        courseRatingSummaryRepository.save(existing);
        return "Course Rating Summary Updated Successfully";
    }

    @Override
    public String delete(String course_id) {
        CourseRatingSummary summary = courseRatingSummaryRepository.findById(course_id);
        if (summary == null) {
            throw new ResourceNotFoundException("Course Rating Summary not found for course_id: " + course_id);
        }
        courseRatingSummaryRepository.delete(course_id);
        return "Course Rating Summary Deleted Successfully";
    }

    private RatingSummaryResponse mapToResponse(CourseRatingSummary summary) {
        RatingSummaryResponse response = new RatingSummaryResponse();
        response.setCourse_id(summary.getCourse_id());
        response.setAverage_rating(summary.getAverage_rating());
        response.setTotal_reviews(summary.getTotal_reviews());
        response.setFive_star_count(summary.getFive_star_count());
        response.setFour_star_count(summary.getFour_star_count());
        response.setThree_star_count(summary.getThree_star_count());
        response.setTwo_star_count(summary.getTwo_star_count());
        response.setOne_star_count(summary.getOne_star_count());
        return response;
    }
}
