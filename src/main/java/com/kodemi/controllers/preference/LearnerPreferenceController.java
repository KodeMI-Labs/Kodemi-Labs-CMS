package com.kodemi.controllers.preference;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.reviewsandratings.preference.ActiveJourneyResponseDto;
import com.kodemi.dto.reviewsandratings.preference.LearnerJourneyProgressDto;
import com.kodemi.dto.reviewsandratings.preference.LearnerPreferenceDto;
import com.kodemi.dto.reviewsandratings.preference.SubmitPreferenceRequest;
import com.kodemi.service.preference.LearnerPreferenceService;

import lombok.RequiredArgsConstructor;

/**
 * Learner-facing preference endpoints.
 * Base URL: http://localhost:8087/learner/preference
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * JOURNEY FETCH
 *   GET  /learner/preference/journey/active          → full journey tree for frontend
 *
 * ONBOARDING LIFECYCLE
 *   POST /learner/preference/start/{learnerId}       → create progress, returns first page
 *   POST /learner/preference/save-page               → partial save per page + advance pointer
 *   POST /learner/preference/submit                  → final submit, mark completed
 *   POST /learner/preference/skip/{learnerId}        → skip entire onboarding
 *
 * READ
 *   GET  /learner/preference/{learnerId}             → saved answers
 *   GET  /learner/preference/progress/{learnerId}    → current page (resume point)
 *   GET  /learner/preference/status/{learnerId}      → { "completed": true/false }
 *
 * ADMIN
 *   GET  /learner/preference/all                     → all learner preferences (analytics)
 *   POST /learner/preference/reset/{learnerId}       → reset so learner can redo onboarding
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/learner/preference")
@RequiredArgsConstructor
public class LearnerPreferenceController {

    private final LearnerPreferenceService preferenceService;

    // ── Journey fetch ─────────────────────────────────────────────────────────

    /**
     * GET /learner/preference/journey/active
     *
     * Called once by the frontend after login.
     * Returns the full journey: pages + embedded options + navigation rules.
     * Frontend renders all onboarding screens dynamically from this response.
     */
    @GetMapping("/journey/active")
    public ActiveJourneyResponseDto getActiveJourney() {
        return preferenceService.getActiveJourney();
    }

    // ── Onboarding lifecycle ──────────────────────────────────────────────────

    /**
     * POST /learner/preference/start/{learnerId}
     *
     * Call when the learner opens the onboarding screen.
     * Creates a progress record and returns current_page_id so the
     * frontend knows which page to show (handles resume automatically).
     */
    @PostMapping("/start/{learnerId}")
    public LearnerJourneyProgressDto startOnboarding(@PathVariable String learnerId) {
        return preferenceService.startOnboarding(learnerId);
    }

    /**
     * POST /learner/preference/save-page
     *
     * Call after each page when learner taps Continue.
     * Merges the answer for that page and advances the progress pointer.
     *
     * Request body:
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
    @PostMapping("/save-page")
    public LearnerPreferenceDto savePageAnswer(@RequestBody SubmitPreferenceRequest request) {
        return preferenceService.savePageAnswer(request);
    }

    /**
     * POST /learner/preference/submit
     *
     * Call when the learner taps Continue on the final page.
     * Saves all answers and marks onboarding as completed = true.
     *
     * Request body:
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
     */
    @PostMapping("/submit")
    public LearnerPreferenceDto submitPreferences(@RequestBody SubmitPreferenceRequest request) {
        return preferenceService.submitPreferences(request);
    }

    /**
     * POST /learner/preference/skip/{learnerId}
     *
     * Call when learner taps "Skip For Now" to bypass the entire onboarding.
     * Marks preferences as skipped = true, completed = true.
     */
    @PostMapping("/skip/{learnerId}")
    public LearnerPreferenceDto skipOnboarding(@PathVariable String learnerId) {
        return preferenceService.skipOnboarding(learnerId);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    /**
     * GET /learner/preference/{learnerId}
     * Returns the learner's saved preferences (all answers).
     */
    @GetMapping("/{learnerId}")
    public LearnerPreferenceDto getPreferences(@PathVariable String learnerId) {
        return preferenceService.getPreferences(learnerId);
    }

    /**
     * GET /learner/preference/progress/{learnerId}
     * Returns the learner's current page position.
     * Used to resume onboarding from where they left off.
     */
    @GetMapping("/progress/{learnerId}")
    public LearnerJourneyProgressDto getProgress(@PathVariable String learnerId) {
        return preferenceService.getProgress(learnerId);
    }

    /**
     * GET /learner/preference/status/{learnerId}
     * Returns { "completed": true } or { "completed": false }.
     * Login flow uses this to decide whether to show onboarding.
     */
    @GetMapping("/status/{learnerId}")
    public Map<String, Boolean> getOnboardingStatus(@PathVariable String learnerId) {
        return Map.of("completed", preferenceService.hasCompletedOnboarding(learnerId));
    }

    // ── Admin / Analytics ─────────────────────────────────────────────────────

    /**
     * GET /learner/preference/all
     * Returns all learner preference records for analytics.
     */
    @GetMapping("/all")
    public List<LearnerPreferenceDto> getAllPreferences() {
        return preferenceService.getAllPreferences();
    }

    /**
     * POST /learner/preference/reset/{learnerId}
     * Resets onboarding so the learner can go through it again.
     */
    @PostMapping("/reset/{learnerId}")
    public Map<String, String> resetPreferences(@PathVariable String learnerId) {
        return Map.of("message", preferenceService.resetPreferences(learnerId));
    }
}
