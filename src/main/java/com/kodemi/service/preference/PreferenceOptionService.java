package com.kodemi.service.preference;
import java.util.List;

import com.kodemi.dto.reviewsandratings.preference.PreferenceOptionDto;

public interface PreferenceOptionService {
    PreferenceOptionDto createOption(PreferenceOptionDto dto);
    PreferenceOptionDto updateOption(String option_id, PreferenceOptionDto dto);
    String deleteOption(String option_id);
    String deactivateOption(String option_id);
    String activateOption(String option_id);
    PreferenceOptionDto getOptionById(String option_id);
    List<PreferenceOptionDto> getOptionsByPageId(String page_id);
    List<PreferenceOptionDto> getActiveOptionsByPageId(String page_id);
    List<PreferenceOptionDto> getChildOptions(String parent_option_id);
}
