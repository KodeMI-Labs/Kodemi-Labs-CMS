package com.ContentManagementSystem.CMS.dto.reviewsandratings.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

    // Used by existing /course-review/create-dto endpoint
    private String course_id;

    // Learner info (passed in by the caller — from auth token or request body)
    private String learner_id;
    private String learner_name;
    private String learner_profile_image;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    private String comment;
}
