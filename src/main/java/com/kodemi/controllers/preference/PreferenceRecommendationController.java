package com.kodemi.controllers.preference;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.reviewsandratings.preference.PreferenceRecommendationDto;
import com.kodemi.service.preference.PreferenceRecommendationService;

import lombok.RequiredArgsConstructor;

/**
 * Preference Recommendation endpoints (Sub Admin + Learner service).
 *
 * Maps learner-selected options to course category recommendations.
 *   Sub Admin configures: option "UI UX" → course category "Design" (priority 1)
 *   Learner service reads: GET /by-option/{option_id} after collecting preferences
 *
 * URL summary:
 *   POST   /preference/recommendation/create                        – create mapping
 *   PUT    /preference/recommendation/update/{recommendation_id}    – update mapping
 *   DELETE /preference/recommendation/delete/{recommendation_id}    – delete mapping
 *   PUT    /preference/recommendation/deactivate/{recommendation_id}– soft-disable
 *   PUT    /preference/recommendation/activate/{recommendation_id}  – re-enable
 *   GET    /preference/recommendation/all                           – all recommendations
 *   GET    /preference/recommendation/{recommendation_id}           – get by id
 *   GET    /preference/recommendation/by-option/{option_id}         – by option (active, sorted by priority)
 *   GET    /preference/recommendation/by-category/{course_category_id} – by category
 */
@RestController
@RequestMapping("/preference/recommendation")
@RequiredArgsConstructor
public class PreferenceRecommendationController {

    private final PreferenceRecommendationService recommendationService;

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    /** POST /preference/recommendation/create */
    @PostMapping("/create")
    public PreferenceRecommendationDto createRecommendation(@RequestBody PreferenceRecommendationDto dto) {
        return recommendationService.createRecommendation(dto);
    }

    /** PUT /preference/recommendation/update/{recommendation_id} */
    @PutMapping("/update/{recommendation_id}")
    public PreferenceRecommendationDto updateRecommendation(
            @PathVariable String recommendation_id,
            @RequestBody PreferenceRecommendationDto dto) {
        return recommendationService.updateRecommendation(recommendation_id, dto);
    }

    /** DELETE /preference/recommendation/delete/{recommendation_id} */
    @DeleteMapping("/delete/{recommendation_id}")
    public String deleteRecommendation(@PathVariable String recommendation_id) {
        return recommendationService.deleteRecommendation(recommendation_id);
    }

    // ── Visibility toggle ─────────────────────────────────────────────────────

    /** PUT /preference/recommendation/deactivate/{recommendation_id} */
    @PutMapping("/deactivate/{recommendation_id}")
    public String deactivateRecommendation(@PathVariable String recommendation_id) {
        return recommendationService.deactivateRecommendation(recommendation_id);
    }

    /** PUT /preference/recommendation/activate/{recommendation_id} */
    @PutMapping("/activate/{recommendation_id}")
    public String activateRecommendation(@PathVariable String recommendation_id) {
        return recommendationService.activateRecommendation(recommendation_id);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    /** GET /preference/recommendation/all */
    @GetMapping("/all")
    public List<PreferenceRecommendationDto> getAllRecommendations() {
        return recommendationService.getAllRecommendations();
    }

    /** GET /preference/recommendation/{recommendation_id} */
    @GetMapping("/{recommendation_id}")
    public PreferenceRecommendationDto getRecommendationById(@PathVariable String recommendation_id) {
        return recommendationService.getRecommendationById(recommendation_id);
    }

    /**
     * GET /preference/recommendation/by-option/{option_id}
     * Returns active recommendations for the given option sorted by priority (asc).
     * This is the endpoint the Learner service calls post-onboarding to build
     * a personalised course recommendation list.
     */
    @GetMapping("/by-option/{option_id}")
    public List<PreferenceRecommendationDto> getActiveRecommendationsByOptionId(@PathVariable String option_id) {
        return recommendationService.getActiveRecommendationsByOptionId(option_id);
    }

    /**
     * GET /preference/recommendation/by-category/{course_category_id}
     * Returns all recommendations mapped to a course category.
     * Useful for Sub Admin analytics.
     */
    @GetMapping("/by-category/{course_category_id}")
    public List<PreferenceRecommendationDto> getRecommendationsByCourseCategoryId(
            @PathVariable String course_category_id) {
        return recommendationService.getRecommendationsByCourseCategoryId(course_category_id);
    }
}
