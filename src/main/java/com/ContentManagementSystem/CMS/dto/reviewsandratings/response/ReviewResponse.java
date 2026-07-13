package com.ContentManagementSystem.CMS.dto.reviewsandratings.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {
    private String reviewId;
    private String courseId;
    private String learnerId;
    private String learnerName;
    private String learnerProfileImage;
    private Integer rating;
    private String comment;
    private String reviewedAt;
    private Boolean edited;
    private Boolean approved;
}
