package com.kodemi.service.preference;

import java.util.List;

import com.kodemi.dto.reviewsandratings.preference.PreferenceRecommendationDto;
public interface PreferenceRecommendationService {
    PreferenceRecommendationDto createRecommendation(PreferenceRecommendationDto dto);
    PreferenceRecommendationDto updateRecommendation(String recommendation_id, PreferenceRecommendationDto dto);
    String deleteRecommendation(String recommendation_id);
    String deactivateRecommendation(String recommendation_id);
    String activateRecommendation(String recommendation_id);
    PreferenceRecommendationDto getRecommendationById(String recommendation_id);
    List<PreferenceRecommendationDto> getAllRecommendations();
    List<PreferenceRecommendationDto> getActiveRecommendationsByOptionId(String option_id);
    List<PreferenceRecommendationDto> getRecommendationsByCourseCategoryId(String course_category_id);
}
