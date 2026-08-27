package com.kodemi.dto.reviewsandratings.preference;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Request body used for both page-by-page partial saves and the final submit.
 *
 * Full submit (POST /learner/preference/submit):
 * {
 *   "learner_id":      "1001",
 *   "journey_id":      "j-001",
 *   "journey_version": 1,
 *   "answers": {
 *     "page-001":       "DESIGNING",
 *     "page-002":       "DESIGN_SKILLS",
 *     "page-003":       "UI_UX",
 *     "page-004-level": "INTERMEDIATE",
 *     "page-004-goal":  "CAREER_GROWTH"
 *   },
 *   "completed": true,
 *   "skipped":   false
 * }
 *
 * Partial page save (POST /learner/preference/save-page):
 * {
 *   "learner_id":         "1001",
 *   "journey_id":         "j-001",
 *   "journey_version":    1,
 *   "answers":            { "page-001": "DESIGNING" },
 *   "current_page_id":    "page-002",
 *   "current_page_order": 2,
 *   "total_pages":        4,
 *   "completed":          false,
 *   "skipped":            false
 * }
 */
@Getter
@Setter
public class SubmitPreferenceRequest {

    private String learner_id;
    private String journey_id;
    private Integer journey_version;

    // page_id → selected option value
    private Map<String, String> answers;

    private Boolean completed;
    private Boolean skipped;

    // Used by save-page only — the page to navigate to next
    private String current_page_id;
    private Integer current_page_order;
    private Integer total_pages;
}
