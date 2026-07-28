package com.ContentManagementSystem.CMS.controller.reviewcontroller;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.request.InstructorReviewRequest;
import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.ReviewResponse;
import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorReview;
import com.ContentManagementSystem.CMS.service.InstructorReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructor-review")
@RequiredArgsConstructor
public class InstructorReviewController {

    private final InstructorReviewService instructorReviewService;

    @PostMapping("/create")
    public String createInstructorReview(@RequestBody InstructorReview instructorReview) {
        return instructorReviewService.createInstructorReview(instructorReview);
    }

    @PostMapping("/create-dto")
    public InstructorReview createInstructorReviewDto(@RequestBody InstructorReviewRequest reviewRequest) {
        return instructorReviewService.createInstructorReview(reviewRequest);
    }

    @GetMapping("/{review_id}")
    public ReviewResponse getInstructorReviewById(@PathVariable String review_id) {
        return instructorReviewService.getInstructorReviewById(review_id);
    }

    @GetMapping("/all")
    public List<ReviewResponse> getAllInstructorReviews() {
        return instructorReviewService.getAllInstructorReviews();
    }

    @GetMapping("/instructor/{instructor_id}")
    public List<ReviewResponse> getReviewsByInstructor(@PathVariable String instructor_id) {
        return instructorReviewService.getReviewsByInstructor(instructor_id);
    }

    @PutMapping("/update/{review_id}")
    public String updateInstructorReview(@PathVariable String review_id, @RequestBody InstructorReview instructorReview) {
        return instructorReviewService.updateInstructorReview(review_id, instructorReview);
    }
    @DeleteMapping("/delete/{review_id}")
    public String deleteInstructorReview(@PathVariable String review_id) {
        return instructorReviewService.delete(review_id);
    }
}
