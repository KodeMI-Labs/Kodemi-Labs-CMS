package com.ContentManagementSystem.CMS.dto.reviewsandratings.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorReviewRequest {
    private String instructor_id;
    private Integer rating;
    private String comment;
}