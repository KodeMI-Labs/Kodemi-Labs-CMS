package com.ContentManagementSystem.CMS.controller.reviewcontroller;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.request.ReviewRequest;
import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.RatingSummaryResponse;
import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.ReviewResponse;
import com.ContentManagementSystem.CMS.model.reviewsandratings.CourseReview;
import com.ContentManagementSystem.CMS.service.CourseRatingSummaryService;
import com.ContentManagementSystem.CMS.service.CourseReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
