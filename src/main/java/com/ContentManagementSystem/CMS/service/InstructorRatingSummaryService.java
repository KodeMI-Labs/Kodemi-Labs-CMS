package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.RatingSummaryResponse;
import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorRatingSummary;

import java.util.List;

public interface InstructorRatingSummaryService {
    InstructorRatingSummary createInstructorRatingSummary(String instructor_id, RatingSummaryResponse ratingSummaryResponse);
    String createInstructorRatingSummary(InstructorRatingSummary instructorRatingSummary);
    RatingSummaryResponse getInstructorRatingSummaryById(String instructor_id);
    List<RatingSummaryResponse> getAllInstructorRatingSummaries();
    String updateInstructorRatingSummary(String instructor_id, InstructorRatingSummary instructorRatingSummary);
    String delete(String instructor_id);
}
