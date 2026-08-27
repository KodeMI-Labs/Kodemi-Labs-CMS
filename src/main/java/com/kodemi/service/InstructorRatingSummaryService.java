package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.response.RatingSummaryResponse;
import com.kodemi.model.reviewsandratings.InstructorRatingSummary;

public interface InstructorRatingSummaryService {
    InstructorRatingSummary createInstructorRatingSummary(String instructor_id, RatingSummaryResponse ratingSummaryResponse);
    String createInstructorRatingSummary(InstructorRatingSummary instructorRatingSummary);
    RatingSummaryResponse getInstructorRatingSummaryById(String instructor_id);
    List<RatingSummaryResponse> getAllInstructorRatingSummaries();
    String updateInstructorRatingSummary(String instructor_id, InstructorRatingSummary instructorRatingSummary);
    String delete(String instructor_id);
}
