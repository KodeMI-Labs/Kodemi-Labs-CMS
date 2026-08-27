package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.request.ReviewRequest;
import com.kodemi.dto.reviewsandratings.response.ReviewResponse;
import com.kodemi.model.reviewsandratings.CourseReview;

public interface CourseReviewService {
	CourseReview createCourseReview(ReviewRequest reviewRequest);

	String createCourseReview(CourseReview courseReview);

	ReviewResponse getCourseReviewById(String review_id);

	List<ReviewResponse> getAllCourseReviews();

	List<ReviewResponse> getReviewsByCourse(String course_id);

	String updateCourseReview(String review_id, CourseReview courseReview);

	String delete(String review_id);

	/**
	 * New: creates review for a course after validating course exists in Course MS.
	 * Also auto-updates the CourseRatingSummary.
	 */
	ReviewResponse createReviewForCourse(String courseId, ReviewRequest reviewRequest);
}
