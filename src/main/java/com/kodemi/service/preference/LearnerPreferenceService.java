package com.kodemi.service.preference;

import java.util.List;

import com.kodemi.dto.reviewsandratings.preference.ActiveJourneyResponseDto;
import com.kodemi.dto.reviewsandratings.preference.LearnerJourneyProgressDto;
import com.kodemi.dto.reviewsandratings.preference.LearnerPreferenceDto;
import com.kodemi.dto.reviewsandratings.preference.SubmitPreferenceRequest;

public interface LearnerPreferenceService {
    ActiveJourneyResponseDto getActiveJourney();

    // ── Onboarding lifecycle ──────────────────────────────────────────────────

    /** Called when the learner opens the onboarding flow for the first time. */
    LearnerJourneyProgressDto startOnboarding(String learnerId);

    /**
     * Partial save — merges the new page answer into the existing answers map
     * and advances the progress pointer to the next page.
     */
    LearnerPreferenceDto savePageAnswer(SubmitPreferenceRequest request);

    /**
     * Final save — saves all answers, marks completed = true, records completed_at.
     */
    LearnerPreferenceDto submitPreferences(SubmitPreferenceRequest request);

    /**
     * Skip all — saves an empty answers map, marks skipped = true, completed = true.
     */
    LearnerPreferenceDto skipOnboarding(String learnerId);

    // ── Read ──────────────────────────────────────────────────────────────────

    LearnerPreferenceDto getPreferences(String learnerId);

    LearnerJourneyProgressDto getProgress(String learnerId);

    /** Used by login flow — true if learner has completed or skipped onboarding. */
    boolean hasCompletedOnboarding(String learnerId);

    // ── Admin / Analytics ─────────────────────────────────────────────────────

    List<LearnerPreferenceDto> getAllPreferences();

    /** Resets answers and progress so the learner can redo onboarding. */
    String resetPreferences(String learnerId);
}
