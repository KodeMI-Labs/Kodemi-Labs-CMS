package com.ContentManagementSystem.CMS.dto.reviewsandratings.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingSummaryResponse {
    private String course_id;
    private Double average_rating;
    private Integer total_reviews;
    private Integer five_star_count;
    private Integer four_star_count;
    private Integer three_star_count;
    private Integer two_star_count;
    private Integer one_star_count;
}
