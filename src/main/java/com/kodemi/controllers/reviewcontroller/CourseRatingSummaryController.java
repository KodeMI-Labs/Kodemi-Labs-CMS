package com.kodemi.controllers.reviewcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.reviewsandratings.response.RatingSummaryResponse;
import com.kodemi.model.reviewsandratings.CourseRatingSummary;
import com.kodemi.service.CourseRatingSummaryService;

import lombok.RequiredArgsConstructor;

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
