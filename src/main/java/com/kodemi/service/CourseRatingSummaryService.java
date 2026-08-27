package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.response.RatingSummaryResponse;
import com.kodemi.model.reviewsandratings.CourseRatingSummary;

public interface CourseRatingSummaryService {
    CourseRatingSummary createCourseRatingSummary(String course_id, RatingSummaryResponse ratingSummaryResponse);
    String createCourseRatingSummary(CourseRatingSummary courseRatingSummary);
    RatingSummaryResponse getRatingSummaryId(String course_id);
    List<RatingSummaryResponse> getAllCourseRatingSummary();
    String updateCourseRatingSummary(String course_id,CourseRatingSummary courseRatingSummary);
    String delete(String course_id);
}
