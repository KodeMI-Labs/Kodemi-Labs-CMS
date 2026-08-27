package com.kodemi.controllers.preference;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.reviewsandratings.preference.ActiveJourneyResponseDto;
import com.kodemi.dto.reviewsandratings.preference.PreferenceJourneyDto;
import com.kodemi.service.preference.PreferenceJourneyService;

import lombok.RequiredArgsConstructor;

/**
 * Preference Journey endpoints.
 *
 * Sub Admin paths  → /preference/journey/**
 * Learner-facing   → /preference/journey/active  (no auth required from CMS side)
 *
 * URL summary:
 *   POST   /preference/journey/create                          – create journey (DRAFT)
 *   PUT    /preference/journey/update/{journey_id}             – update journey details
 *   DELETE /preference/journey/delete/{journey_id}             – delete journey
 *   PUT    /preference/journey/publish/{journey_id}            – publish journey
 *   PUT    /preference/journey/archive/{journey_id}            – archive journey
 *   GET    /preference/journey/all                             – get all journeys
 *   GET    /preference/journey/{journey_id}                    – get by id
 *   GET    /preference/journey/status/{status}                 – filter by status
 *   GET    /preference/journey/active                          – learner-facing: full journey tree
 */
@RestController
@RequestMapping("/preference/journey")
@RequiredArgsConstructor
public class PreferenceJourneyController {

    private final PreferenceJourneyService journeyService;

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    /** POST /preference/journey/create */
    @PostMapping("/create")
    public PreferenceJourneyDto createJourney(@RequestBody PreferenceJourneyDto dto) {
        return journeyService.createJourney(dto);
    }

    /** PUT /preference/journey/update/{journey_id} */
    @PutMapping("/update/{journey_id}")
    public PreferenceJourneyDto updateJourney(
            @PathVariable String journey_id,
            @RequestBody PreferenceJourneyDto dto) {
        return journeyService.updateJourney(journey_id, dto);
    }

    /** DELETE /preference/journey/delete/{journey_id} */
    @DeleteMapping("/delete/{journey_id}")
    public String deleteJourney(@PathVariable String journey_id) {
        return journeyService.deleteJourney(journey_id);
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    /**
     * PUT /preference/journey/publish/{journey_id}?remarks=...
     * Publishes the journey:
     *   - Archives any currently published journey
     *   - Sets this one to PUBLISHED / active = true
     *   - Creates a version snapshot
     */
    @PutMapping("/publish/{journey_id}")
    public PreferenceJourneyDto publishJourney(
            @PathVariable String journey_id,
            @RequestParam(required = false) String remarks) {
        return journeyService.publishJourney(journey_id, remarks);
    }

    /** PUT /preference/journey/archive/{journey_id} */
    @PutMapping("/archive/{journey_id}")
    public PreferenceJourneyDto archiveJourney(@PathVariable String journey_id) {
        return journeyService.archiveJourney(journey_id);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    /** GET /preference/journey/all */
    @GetMapping("/all")
    public List<PreferenceJourneyDto> getAllJourneys() {
        return journeyService.getAllJourneys();
    }

    /** GET /preference/journey/{journey_id} */
    @GetMapping("/{journey_id}")
    public PreferenceJourneyDto getJourneyById(@PathVariable String journey_id) {
        return journeyService.getJourneyById(journey_id);
    }

    /** GET /preference/journey/status/{status}  (DRAFT | PUBLISHED | ARCHIVED) */
    @GetMapping("/status/{status}")
    public List<PreferenceJourneyDto> getJourneysByStatus(@PathVariable String status) {
        return journeyService.getJourneysByStatus(status);
    }

    // ── Learner-facing ────────────────────────────────────────────────────────

    /**
     * GET /preference/journey/active
     *
     * Returns the fully composed active journey:
     *   journey metadata + pages (with embedded options) + navigation rules
     *
     * This is the ONLY endpoint the Learner frontend needs to render
     * the entire dynamic onboarding flow.
     */
    @GetMapping("/active")
    public ActiveJourneyResponseDto getActiveJourney() {
        return journeyService.getActiveJourney();
    }
}
