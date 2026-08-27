package com.kodemi.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.client.CourseServiceClient;
import com.kodemi.dto.reviewsandratings.request.ReviewRequest;
import com.kodemi.dto.reviewsandratings.response.ReviewResponse;
import com.kodemi.exception.BadRequestException;
import com.kodemi.model.reviewsandratings.CourseRatingSummary;
import com.kodemi.model.reviewsandratings.CourseReview;
import com.kodemi.repository.reviewsandratingrepository.CourseRatingSummaryRepository;
import com.kodemi.repository.reviewsandratingrepository.CourseReviewRepository;
import com.kodemi.service.CourseReviewService;

@Service
public class CourseReviewServiceImpl implements CourseReviewService {

    private final CourseReviewRepository courseReviewRepository;
    private final CourseRatingSummaryRepository courseRatingSummaryRepository;
    private final CourseServiceClient courseServiceClient;

    public CourseReviewServiceImpl(
            CourseReviewRepository courseReviewRepository,
            CourseRatingSummaryRepository courseRatingSummaryRepository,
            CourseServiceClient courseServiceClient) {
        this.courseReviewRepository = courseReviewRepository;
        this.courseRatingSummaryRepository = courseRatingSummaryRepository;
        this.courseServiceClient = courseServiceClient;
    }

    // ─── New endpoint: POST /courses/{courseId}/reviews ──────────────────────────
    @Override
    public ReviewResponse createReviewForCourse(String courseId, ReviewRequest reviewRequest) {
        // 1. Validate course exists in Course MS via Feign
        boolean exists = courseServiceClient.courseExists(courseId);
        if (!exists) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        // 2. Validate rating range
        if (reviewRequest.getRating() == null || reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        // 3. Save the review
        CourseReview review = new CourseReview();
        review.setReview_id(UUID.randomUUID().toString());
        review.setCourse_id(courseId);
        review.setLearner_id(reviewRequest.getLearner_id());
        review.setLearner_name(reviewRequest.getLearner_name());
        review.setLearner_profile_image(reviewRequest.getLearner_profile_image());
        review.setRating(reviewRequest.getRating());
        review.setReview_comment(reviewRequest.getComment());
        review.setReview_at(LocalDateTime.now().toString());
        review.setEdited(false);
        review.setApproved(false);
        courseReviewRepository.save(review);

        // 4. Auto-update CourseRatingSummary
        updateRatingSummary(courseId, reviewRequest.getRating());

        return mapToResponse(review);
    }

    // ─── Existing endpoints ───────────────────────────────────────────────────────

    @Override
    public CourseReview createCourseReview(ReviewRequest reviewRequest) {
        CourseReview courseReview = new CourseReview();
        courseReview.setReview_id(UUID.randomUUID().toString());
        courseReview.setCourse_id(reviewRequest.getCourse_id());
        courseReview.setLearner_id(reviewRequest.getLearner_id());
        courseReview.setLearner_name(reviewRequest.getLearner_name());
        courseReview.setLearner_profile_image(reviewRequest.getLearner_profile_image());
        courseReview.setRating(reviewRequest.getRating());
        courseReview.setReview_comment(reviewRequest.getComment());
        courseReview.setReview_at(LocalDateTime.now().toString());
        courseReview.setEdited(false);
        courseReview.setApproved(false);
        courseReviewRepository.save(courseReview);
        
        // Update rating summary
        if (reviewRequest.getCourse_id() != null && reviewRequest.getRating() != null) {
            updateRatingSummaryFromAllReviews(reviewRequest.getCourse_id());
        }
        
        return courseReview;
    }

    @Override
    public String createCourseReview(CourseReview courseReview) {
        courseReview.setReview_id(UUID.randomUUID().toString());
        courseReview.setReview_at(LocalDateTime.now().toString());
        courseReviewRepository.save(courseReview);
        
        // Update rating summary
        if (courseReview.getCourse_id() != null) {
            updateRatingSummaryFromAllReviews(courseReview.getCourse_id());
        }
        
        return "Course Review Created Successfully";
    }

    @Override
    public ReviewResponse getCourseReviewById(String review_id) {
        CourseReview courseReview = courseReviewRepository.findById(review_id);
        if (courseReview == null) {
            throw new ResourceNotFoundException("Course Review not found with id: " + review_id);
        }
        return mapToResponse(courseReview);
    }

    @Override
    public List<ReviewResponse> getAllCourseReviews() {
        List<CourseReview> reviews = courseReviewRepository.findAll();
        List<ReviewResponse> responses = new ArrayList<>();
        for (CourseReview review : reviews) {
            responses.add(mapToResponse(review));
        }
        return responses;
    }

    @Override
    public List<ReviewResponse> getReviewsByCourse(String course_id) {
        // Uses GSI query — efficient
        List<CourseReview> reviews = courseReviewRepository.findByCourseId(course_id);
        List<ReviewResponse> responses = new ArrayList<>();
        for (CourseReview review : reviews) {
            responses.add(mapToResponse(review));
        }
        return responses;
    }

    @Override
    public String updateCourseReview(String review_id, CourseReview courseReview) {
        CourseReview existing = courseReviewRepository.findById(review_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Course Review not found with id: " + review_id);
        }
        existing.setRating(courseReview.getRating());
        existing.setReview_comment(courseReview.getReview_comment());
        existing.setEdited(true);
        existing.setApproved(courseReview.getApproved());
        courseReviewRepository.save(existing);
        
        // Update rating summary after modification
        if (existing.getCourse_id() != null) {
            updateRatingSummaryFromAllReviews(existing.getCourse_id());
        }
        
        return "Course Review Updated Successfully";
    }

    @Override
    public String delete(String review_id) {
        CourseReview courseReview = courseReviewRepository.findById(review_id);
        if (courseReview == null) {
            throw new ResourceNotFoundException("Course Review not found with id: " + review_id);
        }
        String courseId = courseReview.getCourse_id();
        courseReviewRepository.delete(review_id);
        
        // Update rating summary after deletion
        if (courseId != null) {
            updateRatingSummaryFromAllReviews(courseId);
        }
        
        return "Course Review Deleted Successfully";
    }

    // ─── Helper: update or create CourseRatingSummary ────────────────────────────

    private void updateRatingSummary(String courseId, int newRating) {
        CourseRatingSummary summary = courseRatingSummaryRepository.findById(courseId);

        if (summary == null) {
            // First review for this course — create summary
            summary = new CourseRatingSummary();
            summary.setCourse_id(courseId);
            summary.setTotal_reviews(0);
            summary.setFive_star_count(0);
            summary.setFour_star_count(0);
            summary.setThree_star_count(0);
            summary.setTwo_star_count(0);
            summary.setOne_star_count(0);
            summary.setAverage_rating(0.0);
        }

        // Increment total and star count
        int total = summary.getTotal_reviews() == null ? 0 : summary.getTotal_reviews();
        double currentAvg = summary.getAverage_rating() == null ? 0.0 : summary.getAverage_rating();

        total += 1;
        incrementStarCount(summary, newRating);

        // Recalculate average: newAvg = ((oldAvg * (total-1)) + newRating) / total
        double newAvg = ((currentAvg * (total - 1)) + newRating) / total;
        // Round to 2 decimal places
        newAvg = Math.round(newAvg * 100.0) / 100.0;

        summary.setTotal_reviews(total);
        summary.setAverage_rating(newAvg);

        courseRatingSummaryRepository.save(summary);
    }

    private void incrementStarCount(CourseRatingSummary summary, int rating) {
        switch (rating) {
            case 5 -> summary.setFive_star_count(nullSafe(summary.getFive_star_count()) + 1);
            case 4 -> summary.setFour_star_count(nullSafe(summary.getFour_star_count()) + 1);
            case 3 -> summary.setThree_star_count(nullSafe(summary.getThree_star_count()) + 1);
            case 2 -> summary.setTwo_star_count(nullSafe(summary.getTwo_star_count()) + 1);
            case 1 -> summary.setOne_star_count(nullSafe(summary.getOne_star_count()) + 1);
            default -> throw new BadRequestException("Invalid rating: " + rating);
        }
    }

    private int nullSafe(Integer value) {
        return value == null ? 0 : value;
    }

    /**
     * Recalculates the rating summary for a course based on ALL its reviews.
     * This ensures accuracy when updating or deleting reviews.
     */
    private void updateRatingSummaryFromAllReviews(String courseId) {
        if (courseId == null) {
            return;
        }

        // Get all reviews for this course
        List<CourseReview> reviews = courseReviewRepository.findByCourseId(courseId);

        // Calculate rating statistics
        int totalReviews = reviews.size();
        int fiveStarCount = 0;
        int fourStarCount = 0;
        int threeStarCount = 0;
        int twoStarCount = 0;
        int oneStarCount = 0;
        double totalRating = 0.0;

        for (CourseReview review : reviews) {
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

        double averageRating = totalReviews > 0 ? Math.round((totalRating / totalReviews) * 100.0) / 100.0 : 0.0;

        // Get or create rating summary
        CourseRatingSummary summary = courseRatingSummaryRepository.findById(courseId);
        if (summary == null) {
            summary = new CourseRatingSummary();
            summary.setCourse_id(courseId);
        }

        // Update rating summary with recalculated values
        summary.setTotal_reviews(totalReviews);
        summary.setAverage_rating(averageRating);
        summary.setFive_star_count(fiveStarCount);
        summary.setFour_star_count(fourStarCount);
        summary.setThree_star_count(threeStarCount);
        summary.setTwo_star_count(twoStarCount);
        summary.setOne_star_count(oneStarCount);

        courseRatingSummaryRepository.save(summary);
    }

    // ─── Mapper ──────────────────────────────────────────────────────────────────

    private ReviewResponse mapToResponse(CourseReview review) {
        ReviewResponse response = new ReviewResponse();
        response.setReviewId(review.getReview_id());
        response.setCourseId(review.getCourse_id());
        response.setLearnerId(review.getLearner_id());
        response.setLearnerName(review.getLearner_name());
        response.setLearnerProfileImage(review.getLearner_profile_image());
        response.setRating(review.getRating());
        response.setComment(review.getReview_comment());
        response.setReviewedAt(review.getReview_at());
        response.setEdited(review.getEdited());
        response.setApproved(review.getApproved());
        return response;
    }
}
