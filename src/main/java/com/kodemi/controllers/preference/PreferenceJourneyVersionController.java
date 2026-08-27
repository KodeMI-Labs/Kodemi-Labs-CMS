package com.kodemi.controllers.preference;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.reviewsandratings.preference.PreferenceJourneyVersionDto;
import com.kodemi.service.preference.PreferenceJourneyVersionService;

import lombok.RequiredArgsConstructor;

/**
 * Preference Journey Version endpoints (Sub Admin).
 *
 * Versions are snapshots created automatically when a journey is published.
 * This controller is primarily read-only; creation happens in PreferenceJourneyService.
 *
 * URL summary:
 *   GET    /preference/version/{version_id}                    – get by id
 *   GET    /preference/version/journey/{journey_id}            – all versions for a journey
 *   GET    /preference/version/journey/{journey_id}/active     – currently active version
 *   DELETE /preference/version/delete/{version_id}             – delete version (admin cleanup)
 */
@RestController
@RequestMapping("/preference/version")
@RequiredArgsConstructor
public class PreferenceJourneyVersionController {

    private final PreferenceJourneyVersionService versionService;

    // ── Read ──────────────────────────────────────────────────────────────────

    /** GET /preference/version/{version_id} */
    @GetMapping("/{version_id}")
    public PreferenceJourneyVersionDto getVersionById(@PathVariable String version_id) {
        return versionService.getVersionById(version_id);
    }

    /**
     * GET /preference/version/journey/{journey_id}
     * Returns all version records for a journey sorted by version number descending
     * (latest first). Used by Sub Admin to view publish history.
     */
    @GetMapping("/journey/{journey_id}")
    public List<PreferenceJourneyVersionDto> getVersionsByJourneyId(@PathVariable String journey_id) {
        return versionService.getVersionsByJourneyId(journey_id);
    }

    /**
     * GET /preference/version/journey/{journey_id}/active
     * Returns the currently active version for a journey.
     * Useful for the Learner service to record which version a learner was onboarded against.
     */
    @GetMapping("/journey/{journey_id}/active")
    public PreferenceJourneyVersionDto getActiveVersion(@PathVariable String journey_id) {
        return versionService.getActiveVersion(journey_id);
    }

    // ── Admin cleanup ─────────────────────────────────────────────────────────

    /** DELETE /preference/version/delete/{version_id} */
    @DeleteMapping("/delete/{version_id}")
    public String deleteVersion(@PathVariable String version_id) {
        return versionService.deleteVersion(version_id);
    }
}
