package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.request.InstructorReviewRequest;
import com.kodemi.dto.reviewsandratings.response.ReviewResponse;
import com.kodemi.model.reviewsandratings.InstructorReview;

public interface InstructorReviewService {
	InstructorReview createInstructorReview(InstructorReviewRequest reviewRequest);

	String createInstructorReview(InstructorReview instructorReview);

	ReviewResponse getInstructorReviewById(String review_id);

	List<ReviewResponse> getAllInstructorReviews();

	List<ReviewResponse> getReviewsByInstructor(String instructor_id);

	String updateInstructorReview(String review_id, InstructorReview instructorReview);

	String delete(String review_id);
}
