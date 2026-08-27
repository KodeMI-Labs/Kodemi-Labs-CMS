package com.kodemi.service.impl.preference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.preference.PreferenceRecommendationDto;
import com.kodemi.model.PreferenceRecommendation;
import com.kodemi.repository.preference.PreferenceRecommendationRepository;
import com.kodemi.service.preference.PreferenceRecommendationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceRecommendationServiceImpl implements PreferenceRecommendationService {

    private final PreferenceRecommendationRepository recommendationRepository;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private PreferenceRecommendation findOrThrow(String recommendation_id) {
        PreferenceRecommendation rec = recommendationRepository.findById(recommendation_id);
        if (rec == null) throw new ResourceNotFoundException("Recommendation not found: " + recommendation_id);
        return rec;
    }

    private PreferenceRecommendationDto toDto(PreferenceRecommendation rec) {
        PreferenceRecommendationDto dto = new PreferenceRecommendationDto();
        BeanUtils.copyProperties(rec, dto);
        return dto;
    }

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    @Override
    public PreferenceRecommendationDto createRecommendation(PreferenceRecommendationDto dto) {
        PreferenceRecommendation rec = new PreferenceRecommendation();
        BeanUtils.copyProperties(dto, rec);
        rec.setRecommendation_id(UUID.randomUUID().toString());
        rec.setActive(true);
        rec.setCreated_at(LocalDateTime.now());
        rec.setUpdated_at(LocalDateTime.now());
        return toDto(recommendationRepository.save(rec));
    }

    @Override
    public PreferenceRecommendationDto updateRecommendation(String recommendation_id, PreferenceRecommendationDto dto) {
        PreferenceRecommendation existing = findOrThrow(recommendation_id);
        existing.setOption_id(dto.getOption_id());
        existing.setCourse_category_id(dto.getCourse_category_id());
        existing.setPriority(dto.getPriority());
        existing.setUpdated_at(LocalDateTime.now());
        return toDto(recommendationRepository.save(existing));
    }

    @Override
    public String deleteRecommendation(String recommendation_id) {
        findOrThrow(recommendation_id);
        recommendationRepository.deleteById(recommendation_id);
        return "Recommendation deleted successfully";
    }

    // ── Visibility toggle ─────────────────────────────────────────────────────

    @Override
    public String deactivateRecommendation(String recommendation_id) {
        PreferenceRecommendation rec = findOrThrow(recommendation_id);
        rec.setActive(false);
        rec.setUpdated_at(LocalDateTime.now());
        recommendationRepository.save(rec);
        return "Recommendation deactivated successfully";
    }

    @Override
    public String activateRecommendation(String recommendation_id) {
        PreferenceRecommendation rec = findOrThrow(recommendation_id);
        rec.setActive(true);
        rec.setUpdated_at(LocalDateTime.now());
        recommendationRepository.save(rec);
        return "Recommendation activated successfully";
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    public PreferenceRecommendationDto getRecommendationById(String recommendation_id) {
        return toDto(findOrThrow(recommendation_id));
    }

    @Override
    public List<PreferenceRecommendationDto> getAllRecommendations() {
        return recommendationRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreferenceRecommendationDto> getActiveRecommendationsByOptionId(String option_id) {
        return recommendationRepository.findActiveByOptionId(option_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreferenceRecommendationDto> getRecommendationsByCourseCategoryId(String course_category_id) {
        return recommendationRepository.findByCourseCategoryId(course_category_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
