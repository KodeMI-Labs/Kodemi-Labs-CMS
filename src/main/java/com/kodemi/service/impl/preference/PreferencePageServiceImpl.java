package com.kodemi.service.impl.preference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.preference.PreferencePageDto;
import com.kodemi.model.PreferencePage;
import com.kodemi.repository.preference.PreferencePageRepository;
import com.kodemi.service.preference.PreferencePageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferencePageServiceImpl implements PreferencePageService {

    private final PreferencePageRepository pageRepository;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private PreferencePage findOrThrow(String page_id) {
        PreferencePage page = pageRepository.findById(page_id);
        if (page == null) throw new ResourceNotFoundException("Preference page not found: " + page_id);
        return page;
    }

    private PreferencePageDto toDto(PreferencePage page) {
        PreferencePageDto dto = new PreferencePageDto();
        BeanUtils.copyProperties(page, dto);
        return dto;
    }

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    @Override
    public PreferencePageDto createPage(PreferencePageDto dto) {
        PreferencePage page = new PreferencePage();
        BeanUtils.copyProperties(dto, page);
        page.setPage_id(UUID.randomUUID().toString());
        page.setActive(true);
        page.setCreated_at(LocalDateTime.now());
        page.setUpdated_at(LocalDateTime.now());
        return toDto(pageRepository.save(page));
    }

    @Override
    public PreferencePageDto updatePage(String page_id, PreferencePageDto dto) {
        PreferencePage existing = findOrThrow(page_id);
        existing.setTitle(dto.getTitle());
        existing.setSubtitle(dto.getSubtitle());
        existing.setDescription(dto.getDescription());
        existing.setPage_order(dto.getPage_order());
        existing.setField_type(dto.getField_type());
        existing.setSelection_type(dto.getSelection_type());
        existing.setSearchable(dto.getSearchable());
        existing.setMandatory(dto.getMandatory());
        existing.setNext_page_id(dto.getNext_page_id());
        existing.setUpdated_at(LocalDateTime.now());
        return toDto(pageRepository.save(existing));
    }

    @Override
    public String deletePage(String page_id) {
        findOrThrow(page_id);
        pageRepository.deleteById(page_id);
        return "Preference page deleted successfully";
    }

    // ── Visibility toggle ─────────────────────────────────────────────────────

    @Override
    public String deactivatePage(String page_id) {
        PreferencePage page = findOrThrow(page_id);
        page.setActive(false);
        page.setUpdated_at(LocalDateTime.now());
        pageRepository.save(page);
        return "Preference page deactivated successfully";
    }

    @Override
    public String activatePage(String page_id) {
        PreferencePage page = findOrThrow(page_id);
        page.setActive(true);
        page.setUpdated_at(LocalDateTime.now());
        pageRepository.save(page);
        return "Preference page activated successfully";
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    public PreferencePageDto getPageById(String page_id) {
        return toDto(findOrThrow(page_id));
    }

    @Override
    public List<PreferencePageDto> getPagesByJourneyId(String journey_id) {
        return pageRepository.findByJourneyId(journey_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreferencePageDto> getActivePagesByJourneyId(String journey_id) {
        return pageRepository.findActiveByJourneyId(journey_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
