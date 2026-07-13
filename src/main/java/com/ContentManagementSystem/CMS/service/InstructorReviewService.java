package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.request.InstructorReviewRequest;
import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.ReviewResponse;
import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorReview;

import java.util.List;

public interface InstructorReviewService {
    InstructorReview createInstructorReview(InstructorReviewRequest reviewRequest);
    String createInstructorReview(InstructorReview instructorReview);
    ReviewResponse getInstructorReviewById(String review_id);
    List<ReviewResponse> getAllInstructorReviews();
    List<ReviewResponse> getReviewsByInstructor(String instructor_id);
    String updateInstructorReview(String review_id, InstructorReview instructorReview);
    String delete(String review_id);
}
