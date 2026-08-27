package com.kodemi.service.preference;

import java.util.List;

import com.kodemi.dto.reviewsandratings.preference.PreferenceJourneyVersionDto;
public interface PreferenceJourneyVersionService {

    PreferenceJourneyVersionDto createVersion(String journey_id, Integer version, String remarks);
    PreferenceJourneyVersionDto getVersionById(String version_id);
    List<PreferenceJourneyVersionDto> getVersionsByJourneyId(String journey_id);
    PreferenceJourneyVersionDto getActiveVersion(String journey_id);
    String deleteVersion(String version_id);
}
