package com.kodemi.service.impl.preference;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.preference.ActiveJourneyResponseDto;
import com.kodemi.dto.reviewsandratings.preference.LearnerJourneyProgressDto;
import com.kodemi.dto.reviewsandratings.preference.LearnerPreferenceDto;
import com.kodemi.dto.reviewsandratings.preference.SubmitPreferenceRequest;
import com.kodemi.model.LearnerJourneyProgress;
import com.kodemi.model.LearnerPreference;
import com.kodemi.repository.preference.LearnerJourneyProgressRepository;
import com.kodemi.repository.preference.LearnerPreferenceRepository;
import com.kodemi.service.preference.LearnerPreferenceService;
import com.kodemi.service.preference.PreferenceJourneyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearnerPreferenceServiceImpl implements LearnerPreferenceService {

    private final LearnerPreferenceRepository      preferenceRepository;
    private final LearnerJourneyProgressRepository progressRepository;
    private final PreferenceJourneyService         journeyService;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private LearnerPreferenceDto toPreferenceDto(LearnerPreference p) {
        LearnerPreferenceDto dto = new LearnerPreferenceDto();
        BeanUtils.copyProperties(p, dto);
        return dto;
    }

    private LearnerJourneyProgressDto toProgressDto(LearnerJourneyProgress pg) {
        LearnerJourneyProgressDto dto = new LearnerJourneyProgressDto();
        BeanUtils.copyProperties(pg, dto);
        return dto;
    }

    /** Finds or creates the progress record for a learner + journey pair. */
    private LearnerJourneyProgress findOrCreateProgress(String learnerId, String journeyId) {
        List<LearnerJourneyProgress> existing = progressRepository.findByLearnerId(learnerId);
        return existing.stream()
                .filter(p -> journeyId.equals(p.getJourney_id()))
                .findFirst()
                .orElseGet(() -> {
                    LearnerJourneyProgress progress = new LearnerJourneyProgress();
                    progress.setProgress_id(UUID.randomUUID().toString());
                    progress.setLearner_id(learnerId);
                    progress.setJourney_id(journeyId);
                    progress.setCompleted_pages(0);
                    progress.setCompleted(false);
                    progress.setStarted_at(LocalDateTime.now());
                    progress.setUpdated_at(LocalDateTime.now());
                    return progressRepository.save(progress);
                });
    }

    // ── Journey fetch ─────────────────────────────────────────────────────────

    @Override
    public ActiveJourneyResponseDto getActiveJourney() {
        // Delegates to the existing PreferenceJourneyService.getActiveJourney()
        // which builds the full tree: journey + pages + options + rules
        return journeyService.getActiveJourney();
    }

    // ── Onboarding lifecycle ──────────────────────────────────────────────────

    @Override
    public LearnerJourneyProgressDto startOnboarding(String learnerId) {
        ActiveJourneyResponseDto journey = getActiveJourney();
        LearnerJourneyProgress progress = findOrCreateProgress(learnerId, journey.getJourney_id());

        // Only initialise page pointer on a fresh start
        if (progress.getCurrent_page_id() == null
                && journey.getPages() != null
                && !journey.getPages().isEmpty()) {

            ActiveJourneyResponseDto.PageWithOptions firstPage = journey.getPages().get(0);
            progress.setCurrent_page_id(firstPage.getPage_id());
            progress.setCurrent_page_order(firstPage.getPage_order());
            progress.setTotal_pages(journey.getPages().size());
            progress.setUpdated_at(LocalDateTime.now());
            progressRepository.save(progress);
        }

        return toProgressDto(progress);
    }

    @Override
    public LearnerPreferenceDto savePageAnswer(SubmitPreferenceRequest request) {
        LearnerPreference preference = preferenceRepository.findByLearnerId(request.getLearner_id());

        if (preference == null) {
            preference = new LearnerPreference();
            preference.setLearner_id(request.getLearner_id());
            preference.setJourney_id(request.getJourney_id());
            preference.setJourney_version(request.getJourney_version());
            preference.setAnswers(new HashMap<>());
            preference.setCompleted(false);
            preference.setSkipped(false);
            preference.setCompleted_pages(0);
            preference.setStarted_at(LocalDateTime.now());
        }

        // Merge the new answer into the existing map
        if (request.getAnswers() != null) {
            if (preference.getAnswers() == null) preference.setAnswers(new HashMap<>());
            preference.getAnswers().putAll(request.getAnswers());
        }

        preference.setCompleted_pages(preference.getAnswers() == null ? 0 : preference.getAnswers().size());
        preference.setUpdated_at(LocalDateTime.now());
        preferenceRepository.save(preference);

        // Advance progress pointer to next page
        LearnerJourneyProgress progress = findOrCreateProgress(
                request.getLearner_id(), request.getJourney_id());
        if (request.getCurrent_page_id() != null) {
            progress.setCurrent_page_id(request.getCurrent_page_id());
            progress.setCurrent_page_order(request.getCurrent_page_order());
            progress.setCompleted_pages(preference.getCompleted_pages());
            if (request.getTotal_pages() != null) {
                progress.setTotal_pages(request.getTotal_pages());
            }
            progress.setUpdated_at(LocalDateTime.now());
            progressRepository.save(progress);
        }

        return toPreferenceDto(preference);
    }

    @Override
    public LearnerPreferenceDto submitPreferences(SubmitPreferenceRequest request) {
        LearnerPreference preference = preferenceRepository.findByLearnerId(request.getLearner_id());
        if (preference == null) {
            preference = new LearnerPreference();
            preference.setLearner_id(request.getLearner_id());
            preference.setStarted_at(LocalDateTime.now());
        }

        preference.setJourney_id(request.getJourney_id());
        preference.setJourney_version(request.getJourney_version());

        if (request.getAnswers() != null) {
            if (preference.getAnswers() == null) preference.setAnswers(new HashMap<>());
            preference.getAnswers().putAll(request.getAnswers());
        }

        preference.setCompleted(Boolean.TRUE.equals(request.getCompleted()));
        preference.setSkipped(Boolean.TRUE.equals(request.getSkipped()));
        preference.setCompleted_pages(preference.getAnswers() == null ? 0 : preference.getAnswers().size());
        preference.setCompleted_at(LocalDateTime.now());
        preference.setUpdated_at(LocalDateTime.now());
        preferenceRepository.save(preference);

        // Mark progress as completed
        progressRepository.findByLearnerId(request.getLearner_id()).stream()
                .filter(p -> request.getJourney_id().equals(p.getJourney_id()))
                .findFirst()
                .ifPresent(p -> {
                    p.setCompleted(true);
                    p.setUpdated_at(LocalDateTime.now());
                    progressRepository.save(p);
                });

        return toPreferenceDto(preference);
    }

    @Override
    public LearnerPreferenceDto skipOnboarding(String learnerId) {
        ActiveJourneyResponseDto journey = getActiveJourney();

        LearnerPreference preference = preferenceRepository.findByLearnerId(learnerId);
        if (preference == null) {
            preference = new LearnerPreference();
            preference.setLearner_id(learnerId);
            preference.setStarted_at(LocalDateTime.now());
        }

        preference.setJourney_id(journey.getJourney_id());
        preference.setJourney_version(journey.getVersion());
        preference.setAnswers(new HashMap<>());
        preference.setCompleted(true);
        preference.setSkipped(true);
        preference.setCompleted_pages(0);
        preference.setCompleted_at(LocalDateTime.now());
        preference.setUpdated_at(LocalDateTime.now());
        preferenceRepository.save(preference);

        // Mark progress completed
        progressRepository.findByLearnerId(learnerId).stream()
                .filter(p -> journey.getJourney_id().equals(p.getJourney_id()))
                .findFirst()
                .ifPresent(p -> {
                    p.setCompleted(true);
                    p.setUpdated_at(LocalDateTime.now());
                    progressRepository.save(p);
                });

        return toPreferenceDto(preference);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    public LearnerPreferenceDto getPreferences(String learnerId) {
        LearnerPreference preference = preferenceRepository.findByLearnerId(learnerId);
        if (preference == null) {
            throw new ResourceNotFoundException("No preferences found for learner: " + learnerId);
        }
        return toPreferenceDto(preference);
    }

    @Override
    public LearnerJourneyProgressDto getProgress(String learnerId) {
        List<LearnerJourneyProgress> list = progressRepository.findByLearnerId(learnerId);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No onboarding progress found for learner: " + learnerId);
        }
        return toProgressDto(list.get(0));
    }

    @Override
    public boolean hasCompletedOnboarding(String learnerId) {
        LearnerPreference preference = preferenceRepository.findByLearnerId(learnerId);
        return preference != null && Boolean.TRUE.equals(preference.getCompleted());
    }

    // ── Admin / Analytics ─────────────────────────────────────────────────────

    @Override
    public List<LearnerPreferenceDto> getAllPreferences() {
        return preferenceRepository.findAll().stream()
                .map(this::toPreferenceDto)
                .collect(Collectors.toList());
    }

    @Override
    public String resetPreferences(String learnerId) {
        preferenceRepository.deleteByLearnerId(learnerId);

        progressRepository.findByLearnerId(learnerId).forEach(p -> {
            p.setCompleted(false);
            p.setCurrent_page_id(null);
            p.setCurrent_page_order(null);
            p.setCompleted_pages(0);
            p.setUpdated_at(LocalDateTime.now());
            progressRepository.save(p);
        });

        return "Preferences reset successfully for learner: " + learnerId;
    }
}
