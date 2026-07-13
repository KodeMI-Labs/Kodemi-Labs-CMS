package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.request.InstructorReviewRequest;
import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.ReviewResponse;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorReview;
import com.ContentManagementSystem.CMS.repository.reviewsandratingrepository.InstructorReviewRepository;
import com.ContentManagementSystem.CMS.service.InstructorReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InstructorReviewServiceImpl implements InstructorReviewService {

    private final InstructorReviewRepository instructorReviewRepository;

    public InstructorReviewServiceImpl(InstructorReviewRepository instructorReviewRepository) {
        this.instructorReviewRepository = instructorReviewRepository;
    }

    @Override
    public InstructorReview createInstructorReview(InstructorReviewRequest reviewRequest) {
        InstructorReview review = new InstructorReview();
        review.setReview_id(UUID.randomUUID().toString());
        review.setInstructor(reviewRequest.getInstructor_id());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setReviewed_at(LocalDateTime.now());
        instructorReviewRepository.save(review);
        return review;
    }

    @Override
    public String createInstructorReview(InstructorReview instructorReview) {
        instructorReview.setReview_id(UUID.randomUUID().toString());
        instructorReview.setReviewed_at(LocalDateTime.now());
        instructorReviewRepository.save(instructorReview);
        return "Instructor Review Created Successfully";
    }

    @Override
    public ReviewResponse getInstructorReviewById(String review_id) {
        InstructorReview review = instructorReviewRepository.findById(review_id);
        if (review == null) {
            throw new ResourceNotFoundException("Instructor Review not found with id: " + review_id);
        }
        return mapToResponse(review);
    }

    @Override
    public List<ReviewResponse> getAllInstructorReviews() {
        List<InstructorReview> reviews = instructorReviewRepository.findAll();
        List<ReviewResponse> responses = new ArrayList<>();
        for (InstructorReview review : reviews) {
            responses.add(mapToResponse(review));
        }
        return responses;
    }

    @Override
    public List<ReviewResponse> getReviewsByInstructor(String instructor_id) {
        List<InstructorReview> all = instructorReviewRepository.findAll();
        List<ReviewResponse> responses = new ArrayList<>();
        for (InstructorReview review : all) {
            if (instructor_id.equals(review.getInstructor())) {
                responses.add(mapToResponse(review));
            }
        }
        return responses;
    }

    @Override
    public String updateInstructorReview(String review_id, InstructorReview instructorReview) {
        InstructorReview existing = instructorReviewRepository.findById(review_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Instructor Review not found with id: " + review_id);
        }
        existing.setRating(instructorReview.getRating());
        existing.setComment(instructorReview.getComment());
        existing.setLearner_name(instructorReview.getLearner_name());
        instructorReviewRepository.save(existing);
        return "Instructor Review Updated Successfully";
    }

    @Override
    public String delete(String review_id) {
        InstructorReview review = instructorReviewRepository.findById(review_id);
        if (review == null) {
            throw new ResourceNotFoundException("Instructor Review not found with id: " + review_id);
        }
        instructorReviewRepository.delete(review_id);
        return "Instructor Review Deleted Successfully";
    }

    private ReviewResponse mapToResponse(InstructorReview review) {
        ReviewResponse response = new ReviewResponse();
        response.setReviewId(review.getReview_id());
        response.setLearnerName(review.getLearner_name());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setReviewedAt(review.getReviewed_at() != null ? review.getReviewed_at().toString() : null);
        return response;
    }
}
