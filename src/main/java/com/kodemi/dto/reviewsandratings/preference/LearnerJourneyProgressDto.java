package com.kodemi.dto.reviewsandratings.preference;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response DTO for a learner's current position in the onboarding journey.
 * Returned by GET /learner/preference/progress/{learner_id}
 *
 * The frontend uses this to:
 *   1. Resume onboarding from the correct page (current_page_id).
 *   2. Render the progress bar (completed_pages / total_pages).
 */
@Getter
@Setter
public class LearnerJourneyProgressDto {

    private String progress_id;
    private String learner_id;
    private String journey_id;
    private String current_page_id;
    private Integer current_page_order;
    private Integer total_pages;
    private Integer completed_pages;
    private Boolean completed;
    private LocalDateTime started_at;
    private LocalDateTime updated_at;
}
