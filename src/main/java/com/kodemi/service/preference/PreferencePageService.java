package com.kodemi.service.preference;

import java.util.List;

import com.kodemi.dto.reviewsandratings.preference.PreferencePageDto;
public interface PreferencePageService {
    PreferencePageDto createPage(PreferencePageDto dto);
    PreferencePageDto updatePage(String page_id, PreferencePageDto dto);
    String deletePage(String page_id);
    String deactivatePage(String page_id);
    String activatePage(String page_id);
    PreferencePageDto getPageById(String page_id);
    List<PreferencePageDto> getPagesByJourneyId(String journey_id);
    List<PreferencePageDto> getActivePagesByJourneyId(String journey_id);
}
