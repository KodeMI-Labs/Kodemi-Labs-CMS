package com.kodemi.dto.reviewsandratings.preference;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PreferenceRecommendationDto {

    private String recommendation_id;
    private String option_id;
    private String course_category_id;
    private Integer priority;
    private Boolean active;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
