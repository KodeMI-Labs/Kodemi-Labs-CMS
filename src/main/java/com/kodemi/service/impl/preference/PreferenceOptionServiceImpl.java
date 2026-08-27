package com.kodemi.service.impl.preference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.preference.PreferenceOptionDto;
import com.kodemi.model.PreferenceOption;
import com.kodemi.repository.preference.PreferenceOptionRepository;
import com.kodemi.service.preference.PreferenceOptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceOptionServiceImpl implements PreferenceOptionService {

    private final PreferenceOptionRepository optionRepository;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private PreferenceOption findOrThrow(String option_id) {
        PreferenceOption option = optionRepository.findById(option_id);
        if (option == null) throw new ResourceNotFoundException("Preference option not found: " + option_id);
        return option;
    }

    private PreferenceOptionDto toDto(PreferenceOption option) {
        PreferenceOptionDto dto = new PreferenceOptionDto();
        BeanUtils.copyProperties(option, dto);
        return dto;
    }

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    @Override
    public PreferenceOptionDto createOption(PreferenceOptionDto dto) {
        PreferenceOption option = new PreferenceOption();
        BeanUtils.copyProperties(dto, option);
        option.setOption_id(UUID.randomUUID().toString());
        option.setActive(true);
        option.setCreated_at(LocalDateTime.now());
        option.setUpdated_at(LocalDateTime.now());
        return toDto(optionRepository.save(option));
    }

    @Override
    public PreferenceOptionDto updateOption(String option_id, PreferenceOptionDto dto) {
        PreferenceOption existing = findOrThrow(option_id);
        existing.setDisplay_name(dto.getDisplay_name());
        existing.setValue(dto.getValue());
        existing.setIcon_url(dto.getIcon_url());
        existing.setImage_url(dto.getImage_url());
        existing.setDisplay_order(dto.getDisplay_order());
        existing.setParent_option_id(dto.getParent_option_id());
        existing.setNext_page_id(dto.getNext_page_id());
        existing.setUpdated_at(LocalDateTime.now());
        return toDto(optionRepository.save(existing));
    }

    @Override
    public String deleteOption(String option_id) {
        findOrThrow(option_id);
        optionRepository.deleteById(option_id);
        return "Preference option deleted successfully";
    }

    // ── Visibility toggle ─────────────────────────────────────────────────────

    @Override
    public String deactivateOption(String option_id) {
        PreferenceOption option = findOrThrow(option_id);
        option.setActive(false);
        option.setUpdated_at(LocalDateTime.now());
        optionRepository.save(option);
        return "Preference option deactivated successfully";
    }

    @Override
    public String activateOption(String option_id) {
        PreferenceOption option = findOrThrow(option_id);
        option.setActive(true);
        option.setUpdated_at(LocalDateTime.now());
        optionRepository.save(option);
        return "Preference option activated successfully";
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    public PreferenceOptionDto getOptionById(String option_id) {
        return toDto(findOrThrow(option_id));
    }

    @Override
    public List<PreferenceOptionDto> getOptionsByPageId(String page_id) {
        return optionRepository.findByPageId(page_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreferenceOptionDto> getActiveOptionsByPageId(String page_id) {
        return optionRepository.findActiveByPageId(page_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreferenceOptionDto> getChildOptions(String parent_option_id) {
        return optionRepository.findByParentOptionId(parent_option_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
