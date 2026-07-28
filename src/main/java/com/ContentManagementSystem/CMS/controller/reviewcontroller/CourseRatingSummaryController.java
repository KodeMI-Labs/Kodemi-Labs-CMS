package com.ContentManagementSystem.CMS.controller.reviewcontroller;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.RatingSummaryResponse;
import com.ContentManagementSystem.CMS.model.reviewsandratings.CourseRatingSummary;
import com.ContentManagementSystem.CMS.service.CourseRatingSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/course-rating-summary")
@RequiredArgsConstructor
public class CourseRatingSummaryController {

    private final CourseRatingSummaryService courseRatingSummaryService;

    @PostMapping("/create")
    public String createCourseRatingSummary(@RequestBody CourseRatingSummary courseRatingSummary) {
        return courseRatingSummaryService.createCourseRatingSummary(courseRatingSummary);
    }

    @PostMapping("/create-dto/{course_id}")
    public CourseRatingSummary createCourseRatingSummaryDto(
            @PathVariable String course_id,
            @RequestBody RatingSummaryResponse ratingSummaryResponse) {
        return courseRatingSummaryService.createCourseRatingSummary(course_id, ratingSummaryResponse);
    }

    @GetMapping("/{course_id}")
    public RatingSummaryResponse getCourseRatingSummaryById(@PathVariable String course_id) {
        return courseRatingSummaryService.getRatingSummaryId(course_id);
    }

    @GetMapping("/all")
    public List<RatingSummaryResponse> getAllCourseRatingSummaries() {
        return courseRatingSummaryService.getAllCourseRatingSummary();
    }

    @PutMapping("/update/{course_id}")
    public String updateCourseRatingSummary(@PathVariable String course_id, @RequestBody CourseRatingSummary courseRatingSummary) {
        return courseRatingSummaryService.updateCourseRatingSummary(course_id, courseRatingSummary);
    }

    @DeleteMapping("/delete/{course_id}")
    public String deleteCourseRatingSummary(@PathVariable String course_id) {
        return courseRatingSummaryService.delete(course_id);
    }
}
