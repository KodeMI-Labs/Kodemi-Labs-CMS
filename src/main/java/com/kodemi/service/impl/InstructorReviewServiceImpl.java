package com.kodemi.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.request.InstructorReviewRequest;
import com.kodemi.dto.reviewsandratings.response.ReviewResponse;
import com.kodemi.model.reviewsandratings.InstructorRatingSummary;
import com.kodemi.model.reviewsandratings.InstructorReview;
import com.kodemi.repository.reviewsandratingrepository.InstructorRatingSummaryRepository;
import com.kodemi.repository.reviewsandratingrepository.InstructorReviewRepository;
import com.kodemi.service.InstructorReviewService;

@Service
public class InstructorReviewServiceImpl implements InstructorReviewService {

    private final InstructorReviewRepository instructorReviewRepository;
    private final InstructorRatingSummaryRepository instructorRatingSummaryRepository;

    public InstructorReviewServiceImpl(InstructorReviewRepository instructorReviewRepository,
                                       InstructorRatingSummaryRepository instructorRatingSummaryRepository) {
        this.instructorReviewRepository = instructorReviewRepository;
        this.instructorRatingSummaryRepository = instructorRatingSummaryRepository;
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
        updateRatingSummary(reviewRequest.getInstructor_id());
        return review;
    }

    @Override
    public String createInstructorReview(InstructorReview instructorReview) {
        instructorReview.setReview_id(UUID.randomUUID().toString());
        instructorReview.setReviewed_at(LocalDateTime.now());
        instructorReviewRepository.save(instructorReview);
        updateRatingSummary(instructorReview.getInstructor());
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
        updateRatingSummary(existing.getInstructor());
        return "Instructor Review Updated Successfully";
    }

    @Override
    public String delete(String review_id) {
        InstructorReview review = instructorReviewRepository.findById(review_id);
        if (review == null) {
            throw new ResourceNotFoundException("Instructor Review not found with id: " + review_id);
        }
        String instructorId = review.getInstructor();
        instructorReviewRepository.delete(review_id);
        updateRatingSummary(instructorId);
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

    /**
     * Updates the rating summary for an instructor based on all their reviews
     */
    private void updateRatingSummary(String instructorId) {
        if (instructorId == null) {
            return;
        }

        // Get all reviews for this instructor
        List<InstructorReview> reviews = instructorReviewRepository.findAll();
        List<InstructorReview> instructorReviews = new ArrayList<>();
        for (InstructorReview review : reviews) {
            if (instructorId.equals(review.getInstructor())) {
                instructorReviews.add(review);
            }
        }

        // Calculate rating statistics
        int totalReviews = instructorReviews.size();
        int fiveStarCount = 0;
        int fourStarCount = 0;
        int threeStarCount = 0;
        int twoStarCount = 0;
        int oneStarCount = 0;
        double totalRating = 0.0;

        for (InstructorReview review : instructorReviews) {
            Integer rating = review.getRating();
            if (rating != null) {
                totalRating += rating;
                switch (rating) {
                    case 5:
                        fiveStarCount++;
                        break;
                    case 4:
                        fourStarCount++;
                        break;
                    case 3:
                        threeStarCount++;
                        break;
                    case 2:
                        twoStarCount++;
                        break;
                    case 1:
                        oneStarCount++;
                        break;
                }
            }
        }

        double averageRating = totalReviews > 0 ? totalRating / totalReviews : 0.0;

        // Get or create rating summary
        InstructorRatingSummary summary = instructorRatingSummaryRepository.findById(instructorId);
        if (summary == null) {
            summary = new InstructorRatingSummary();
            summary.setInstructor_id(instructorId);
        }

        // Update rating summary
        summary.setTotal_reviews(totalReviews);
        summary.setAverage_rating(averageRating);
        summary.setFive_star_count(fiveStarCount);
        summary.setFour_star_count(fourStarCount);
        summary.setThree_star_count(threeStarCount);
        summary.setTwo_star_count(twoStarCount);
        summary.setOne_star_count(oneStarCount);

        instructorRatingSummaryRepository.save(summary);
    }
}
