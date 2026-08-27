package com.kodemi.service.impl.preference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.preference.ActiveJourneyResponseDto;
import com.kodemi.dto.reviewsandratings.preference.PreferenceJourneyDto;
import com.kodemi.dto.reviewsandratings.preference.PreferenceRuleDto;
import com.kodemi.model.PreferenceJourney;
import com.kodemi.model.PreferenceOption;
import com.kodemi.model.PreferencePage;
import com.kodemi.repository.preference.PreferenceJourneyRepository;
import com.kodemi.repository.preference.PreferenceOptionRepository;
import com.kodemi.repository.preference.PreferencePageRepository;
import com.kodemi.repository.preference.PreferenceRuleRepository;
import com.kodemi.service.preference.PreferenceJourneyService;
import com.kodemi.service.preference.PreferenceJourneyVersionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceJourneyServiceImpl implements PreferenceJourneyService {

    private final PreferenceJourneyRepository journeyRepository;
    private final PreferencePageRepository    pageRepository;
    private final PreferenceOptionRepository  optionRepository;
    private final PreferenceRuleRepository    ruleRepository;

    // Injected lazily via setter to avoid circular dependency
    private final PreferenceJourneyVersionService versionService;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private PreferenceJourney findOrThrow(String journey_id) {
        PreferenceJourney j = journeyRepository.findById(journey_id);
        if (j == null) throw new ResourceNotFoundException("Journey not found: " + journey_id);
        return j;
    }

    private PreferenceJourneyDto toDto(PreferenceJourney j) {
        PreferenceJourneyDto dto = new PreferenceJourneyDto();
        BeanUtils.copyProperties(j, dto);
        return dto;
    }

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    @Override
    public PreferenceJourneyDto createJourney(PreferenceJourneyDto dto) {
        PreferenceJourney journey = new PreferenceJourney();
        BeanUtils.copyProperties(dto, journey);
        journey.setJourney_id(UUID.randomUUID().toString());
        journey.setStatus("DRAFT");
        journey.setActive(false);
        journey.setVersion(1);
        journey.setCreated_at(LocalDateTime.now());
        journey.setUpdated_at(LocalDateTime.now());
        return toDto(journeyRepository.save(journey));
    }

    @Override
    public PreferenceJourneyDto updateJourney(String journey_id, PreferenceJourneyDto dto) {
        PreferenceJourney existing = findOrThrow(journey_id);
        existing.setJourney_name(dto.getJourney_name());
        existing.setDescription(dto.getDescription());
        existing.setUpdated_by(dto.getUpdated_by());
        existing.setUpdated_at(LocalDateTime.now());
        return toDto(journeyRepository.save(existing));
    }

    @Override
    public String deleteJourney(String journey_id) {
        findOrThrow(journey_id);
        journeyRepository.deleteById(journey_id);
        return "Journey deleted successfully";
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    public PreferenceJourneyDto publishJourney(String journey_id, String remarks) {
        PreferenceJourney journey = findOrThrow(journey_id);

        // Deactivate any currently published journey (single-active invariant)
        journeyRepository.findActiveJourneys().forEach(active -> {
            if (!active.getJourney_id().equals(journey_id)) {
                active.setStatus("ARCHIVED");
                active.setActive(false);
                active.setUpdated_at(LocalDateTime.now());
                journeyRepository.save(active);
            }
        });

        // Determine next version number
        int nextVersion = journey.getVersion() == null ? 1 : journey.getVersion() + 1;

        journey.setStatus("PUBLISHED");
        journey.setActive(true);
        journey.setVersion(nextVersion);
        journey.setPublish_date(LocalDateTime.now());
        journey.setUpdated_at(LocalDateTime.now());
        journeyRepository.save(journey);

        // Snapshot
        versionService.createVersion(journey_id, nextVersion, remarks);

        return toDto(journey);
    }

    @Override
    public PreferenceJourneyDto archiveJourney(String journey_id) {
        PreferenceJourney journey = findOrThrow(journey_id);
        journey.setStatus("ARCHIVED");
        journey.setActive(false);
        journey.setUpdated_at(LocalDateTime.now());
        return toDto(journeyRepository.save(journey));
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    public PreferenceJourneyDto getJourneyById(String journey_id) {
        return toDto(findOrThrow(journey_id));
    }

    @Override
    public List<PreferenceJourneyDto> getAllJourneys() {
        return journeyRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreferenceJourneyDto> getJourneysByStatus(String status) {
        return journeyRepository.findByStatus(status).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public ActiveJourneyResponseDto getActiveJourney() {
        List<PreferenceJourney> active = journeyRepository.findActiveJourneys();
        if (active.isEmpty()) {
            throw new ResourceNotFoundException("No active preference journey found. Please publish a journey first.");
        }
        PreferenceJourney journey = active.get(0);

        ActiveJourneyResponseDto response = new ActiveJourneyResponseDto();
        response.setJourney_id(journey.getJourney_id());
        response.setJourney_name(journey.getJourney_name());
        response.setDescription(journey.getDescription());
        response.setVersion(journey.getVersion());
        response.setPublish_date(journey.getPublish_date());

        // ── Build pages with embedded options ─────────────────────────────────
        List<PreferencePage> pages = pageRepository.findActiveByJourneyId(journey.getJourney_id());
        List<ActiveJourneyResponseDto.PageWithOptions> pageList = new ArrayList<>();
        List<ActiveJourneyResponseDto.OptionSummary> allOptionSummaries = new ArrayList<>();

        for (PreferencePage page : pages) {
            ActiveJourneyResponseDto.PageWithOptions pageDto = new ActiveJourneyResponseDto.PageWithOptions();
            pageDto.setPage_id(page.getPage_id());
            pageDto.setPage_order(page.getPage_order());
            pageDto.setTitle(page.getTitle());
            pageDto.setSubtitle(page.getSubtitle());
            pageDto.setDescription(page.getDescription());
            pageDto.setField_type(page.getField_type());
            pageDto.setSelection_type(page.getSelection_type());
            pageDto.setSearchable(page.getSearchable());
            pageDto.setMandatory(page.getMandatory());
            pageDto.setNext_page_id(page.getNext_page_id());

            // Embed active options for this page
            List<PreferenceOption> options = optionRepository.findActiveByPageId(page.getPage_id());
            List<ActiveJourneyResponseDto.OptionSummary> optionSummaries = options.stream()
                    .map(opt -> {
                        ActiveJourneyResponseDto.OptionSummary summary = new ActiveJourneyResponseDto.OptionSummary();
                        summary.setOption_id(opt.getOption_id());
                        summary.setDisplay_name(opt.getDisplay_name());
                        summary.setValue(opt.getValue());
                        summary.setIcon_url(opt.getIcon_url());
                        summary.setImage_url(opt.getImage_url());
                        summary.setDisplay_order(opt.getDisplay_order());
                        summary.setParent_option_id(opt.getParent_option_id());
                        summary.setNext_page_id(opt.getNext_page_id());
                        return summary;
                    })
                    .collect(Collectors.toList());

            pageDto.setOptions(optionSummaries);
            allOptionSummaries.addAll(optionSummaries);
            pageList.add(pageDto);
        }
        response.setPages(pageList);

        // ── Build navigation rules ─────────────────────────────────────────────
        // Collect rules for all pages in this journey
        List<PreferenceRuleDto> ruleDtos = pages.stream()
                .flatMap(page -> ruleRepository.findBySourcePageId(page.getPage_id()).stream())
                .filter(rule -> Boolean.TRUE.equals(rule.getActive()))
                .map(rule -> {
                    PreferenceRuleDto rDto = new PreferenceRuleDto();
                    BeanUtils.copyProperties(rule, rDto);
                    return rDto;
                })
                .collect(Collectors.toList());
        response.setRules(ruleDtos);

        return response;
    }
}
