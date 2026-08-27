package com.kodemi.service.preference;

import java.util.List;

import com.kodemi.dto.reviewsandratings.preference.PreferenceRuleDto;
public interface PreferenceRuleService {

    PreferenceRuleDto createRule(PreferenceRuleDto dto);
    PreferenceRuleDto updateRule(String rule_id, PreferenceRuleDto dto);
    String deleteRule(String rule_id);
    String deactivateRule(String rule_id);
    String activateRule(String rule_id);
    PreferenceRuleDto getRuleById(String rule_id);
    List<PreferenceRuleDto> getAllRules();
    List<PreferenceRuleDto> getRulesBySourcePageId(String source_page_id);
    List<PreferenceRuleDto> getRulesBySourceOptionId(String source_option_id);
    List<PreferenceRuleDto> getRulesByTargetPageId(String target_page_id);
}
