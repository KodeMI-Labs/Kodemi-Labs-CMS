package com.kodemi.dto.reviewsandratings.preference;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response DTO for a learner's saved preferences.
 * Returned by GET /learner/preference/{learner_id}
 */
@Getter
@Setter
public class LearnerPreferenceDto {

    private String learner_id;
    private String journey_id;
    private Integer journey_version;

    /**
     * Map of page_id → selected option value.
     * Example:
     *   "page-001"       → "DESIGNING"
     *   "page-002"       → "DESIGN_SKILLS"
     *   "page-003"       → "UI_UX"
     *   "page-004-level" → "INTERMEDIATE"
     *   "page-004-goal"  → "CAREER_GROWTH"
     */
    private Map<String, String> answers;

    private Boolean completed;
    private Integer completed_pages;
    private Boolean skipped;

    private LocalDateTime started_at;
    private LocalDateTime completed_at;
    private LocalDateTime updated_at;
}
