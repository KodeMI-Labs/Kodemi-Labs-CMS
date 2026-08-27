
package com.kodemi.service.impl.preference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.preference.PreferenceRuleDto;
import com.kodemi.model.PreferenceRule;
import com.kodemi.repository.preference.PreferenceRuleRepository;
import com.kodemi.service.preference.PreferenceRuleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceRuleServiceImpl implements PreferenceRuleService {

    private final PreferenceRuleRepository ruleRepository;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private PreferenceRule findOrThrow(String rule_id) {
        PreferenceRule rule = ruleRepository.findById(rule_id);
        if (rule == null) throw new ResourceNotFoundException("Preference rule not found: " + rule_id);
        return rule;
    }

    private PreferenceRuleDto toDto(PreferenceRule rule) {
        PreferenceRuleDto dto = new PreferenceRuleDto();
        BeanUtils.copyProperties(rule, dto);
        return dto;
    }

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    @Override
    public PreferenceRuleDto createRule(PreferenceRuleDto dto) {
        PreferenceRule rule = new PreferenceRule();
        BeanUtils.copyProperties(dto, rule);
        rule.setRule_id(UUID.randomUUID().toString());
        rule.setActive(true);
        rule.setCreated_at(LocalDateTime.now());
        rule.setUpdated_at(LocalDateTime.now());
        return toDto(ruleRepository.save(rule));
    }

    @Override
    public PreferenceRuleDto updateRule(String rule_id, PreferenceRuleDto dto) {
        PreferenceRule existing = findOrThrow(rule_id);
        existing.setSource_page_id(dto.getSource_page_id());
        existing.setSource_option_id(dto.getSource_option_id());
        existing.setTarget_page_id(dto.getTarget_page_id());
        existing.setRule_type(dto.getRule_type());
        existing.setUpdated_at(LocalDateTime.now());
        return toDto(ruleRepository.save(existing));
    }

    @Override
    public String deleteRule(String rule_id) {
        findOrThrow(rule_id);
        ruleRepository.deleteById(rule_id);
        return "Preference rule deleted successfully";
    }

    // ── Visibility toggle ─────────────────────────────────────────────────────

    @Override
    public String deactivateRule(String rule_id) {
        PreferenceRule rule = findOrThrow(rule_id);
        rule.setActive(false);
        rule.setUpdated_at(LocalDateTime.now());
        ruleRepository.save(rule);
        return "Preference rule deactivated successfully";
    }

    @Override
    public String activateRule(String rule_id) {
        PreferenceRule rule = findOrThrow(rule_id);
        rule.setActive(true);
        rule.setUpdated_at(LocalDateTime.now());
        ruleRepository.save(rule);
        return "Preference rule activated successfully";
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    public PreferenceRuleDto getRuleById(String rule_id) {
        return toDto(findOrThrow(rule_id));
    }

    @Override
    public List<PreferenceRuleDto> getAllRules() {
        return ruleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreferenceRuleDto> getRulesBySourcePageId(String source_page_id) {
        return ruleRepository.findBySourcePageId(source_page_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreferenceRuleDto> getRulesBySourceOptionId(String source_option_id) {
        return ruleRepository.findBySourceOptionId(source_option_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreferenceRuleDto> getRulesByTargetPageId(String target_page_id) {
        return ruleRepository.findByTargetPageId(target_page_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
