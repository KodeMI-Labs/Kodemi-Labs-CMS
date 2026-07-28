package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.RatingSummaryResponse;
import com.ContentManagementSystem.CMS.model.reviewsandratings.CourseRatingSummary;

import java.util.List;

public interface CourseRatingSummaryService {
    CourseRatingSummary createCourseRatingSummary(String course_id, RatingSummaryResponse ratingSummaryResponse);
    String createCourseRatingSummary(CourseRatingSummary courseRatingSummary);
    RatingSummaryResponse getRatingSummaryId(String course_id);
    List<RatingSummaryResponse> getAllCourseRatingSummary();
    String updateCourseRatingSummary(String course_id,CourseRatingSummary courseRatingSummary);
    String delete(String course_id);
}
