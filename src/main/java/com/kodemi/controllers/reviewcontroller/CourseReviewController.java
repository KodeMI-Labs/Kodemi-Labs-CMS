package com.kodemi.controllers.reviewcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.reviewsandratings.request.ReviewRequest;
import com.kodemi.dto.reviewsandratings.response.RatingSummaryResponse;
import com.kodemi.dto.reviewsandratings.response.ReviewResponse;
import com.kodemi.model.reviewsandratings.CourseReview;
import com.kodemi.service.CourseRatingSummaryService;
import com.kodemi.service.CourseReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CourseReviewController {

    private final CourseReviewService courseReviewService;
    private final CourseRatingSummaryService courseRatingSummaryService;
    @PostMapping("/courses/{courseId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createReviewForCourse(
            @PathVariable String courseId,
            @RequestBody ReviewRequest reviewRequest) {
        return courseReviewService.createReviewForCourse(courseId, reviewRequest);
    }
    @GetMapping("/courses/{courseId}/reviews")
    public List<ReviewResponse> getReviewsByCourseId(@PathVariable String courseId) {
        return courseReviewService.getReviewsByCourse(courseId);
    }
    @GetMapping("/courses/{courseId}/rating-summary")
    public RatingSummaryResponse getRatingSummaryByCourseId(@PathVariable String courseId) {
        return courseRatingSummaryService.getRatingSummaryId(courseId);
    }
    @PostMapping("/course-review/create")
    public String createCourseReview(@RequestBody CourseReview courseReview) {
        return courseReviewService.createCourseReview(courseReview);
    }

    @PostMapping("/course-review/create-dto")
    public CourseReview createCourseReviewDto(@RequestBody ReviewRequest reviewRequest) {
        return courseReviewService.createCourseReview(reviewRequest);
    }

    @GetMapping("/course-review/{review_id}")
    public ReviewResponse getCourseReviewById(@PathVariable String review_id) {
        return courseReviewService.getCourseReviewById(review_id);
    }

    @GetMapping("/course-review/all")
    public List<ReviewResponse> getAllCourseReviews() {
        return courseReviewService.getAllCourseReviews();
    }

    @GetMapping("/course-review/course/{course_id}")
    public List<ReviewResponse> getReviewsByCourse(@PathVariable String course_id) {
        return courseReviewService.getReviewsByCourse(course_id);
    }

    @PutMapping("/course-review/update/{review_id}")
    public String updateCourseReview(@PathVariable String review_id, @RequestBody CourseReview courseReview) {
        return courseReviewService.updateCourseReview(review_id, courseReview);
    }

    @DeleteMapping("/course-review/delete/{review_id}")
    public String deleteCourseReview(@PathVariable String review_id) {
        return courseReviewService.delete(review_id);
    }
}
