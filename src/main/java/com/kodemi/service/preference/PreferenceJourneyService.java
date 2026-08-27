package com.kodemi.service.preference;

import java.util.List;

import com.kodemi.dto.reviewsandratings.preference.ActiveJourneyResponseDto;
import com.kodemi.dto.reviewsandratings.preference.PreferenceJourneyDto;
public interface PreferenceJourneyService {
    PreferenceJourneyDto createJourney(PreferenceJourneyDto dto);
    PreferenceJourneyDto updateJourney(String journey_id, PreferenceJourneyDto dto);
    String deleteJourney(String journey_id);
    PreferenceJourneyDto publishJourney(String journey_id, String remarks);
    PreferenceJourneyDto archiveJourney(String journey_id);
    PreferenceJourneyDto getJourneyById(String journey_id);
    List<PreferenceJourneyDto> getAllJourneys();
    List<PreferenceJourneyDto> getJourneysByStatus(String status);
    ActiveJourneyResponseDto getActiveJourney();
}
