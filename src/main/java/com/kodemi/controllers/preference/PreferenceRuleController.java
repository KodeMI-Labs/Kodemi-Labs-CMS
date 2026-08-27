package com.kodemi.controllers.preference;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.reviewsandratings.preference.PreferenceRuleDto;
import com.kodemi.service.preference.PreferenceRuleService;

import lombok.RequiredArgsConstructor;

/**
 * Preference Rule endpoints (Sub Admin).
 *
 * Rules define conditional navigation:
 *   IF learner selects option X on page A → navigate to page B
 *
 * URL summary:
 *   POST   /preference/rule/create                             – create rule
 *   PUT    /preference/rule/update/{rule_id}                   – update rule
 *   DELETE /preference/rule/delete/{rule_id}                   – delete rule
 *   PUT    /preference/rule/deactivate/{rule_id}               – soft-disable
 *   PUT    /preference/rule/activate/{rule_id}                 – re-enable
 *   GET    /preference/rule/all                                – all rules
 *   GET    /preference/rule/{rule_id}                          – get by id
 *   GET    /preference/rule/source-page/{source_page_id}       – rules from a page
 *   GET    /preference/rule/source-option/{source_option_id}   – rules from an option
 *   GET    /preference/rule/target-page/{target_page_id}       – rules pointing to a page
 */
@RestController
@RequestMapping("/preference/rule")
@RequiredArgsConstructor
public class PreferenceRuleController {

    private final PreferenceRuleService ruleService;

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    /** POST /preference/rule/create */
    @PostMapping("/create")
    public PreferenceRuleDto createRule(@RequestBody PreferenceRuleDto dto) {
        return ruleService.createRule(dto);
    }

    /** PUT /preference/rule/update/{rule_id} */
    @PutMapping("/update/{rule_id}")
    public PreferenceRuleDto updateRule(
            @PathVariable String rule_id,
            @RequestBody PreferenceRuleDto dto) {
        return ruleService.updateRule(rule_id, dto);
    }

    /** DELETE /preference/rule/delete/{rule_id} */
    @DeleteMapping("/delete/{rule_id}")
    public String deleteRule(@PathVariable String rule_id) {
        return ruleService.deleteRule(rule_id);
    }

    // ── Visibility toggle ─────────────────────────────────────────────────────

    /** PUT /preference/rule/deactivate/{rule_id} */
    @PutMapping("/deactivate/{rule_id}")
    public String deactivateRule(@PathVariable String rule_id) {
        return ruleService.deactivateRule(rule_id);
    }

    /** PUT /preference/rule/activate/{rule_id} */
    @PutMapping("/activate/{rule_id}")
    public String activateRule(@PathVariable String rule_id) {
        return ruleService.activateRule(rule_id);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    /** GET /preference/rule/all */
    @GetMapping("/all")
    public List<PreferenceRuleDto> getAllRules() {
        return ruleService.getAllRules();
    }

    /** GET /preference/rule/{rule_id} */
    @GetMapping("/{rule_id}")
    public PreferenceRuleDto getRuleById(@PathVariable String rule_id) {
        return ruleService.getRuleById(rule_id);
    }

    /** GET /preference/rule/source-page/{source_page_id} */
    @GetMapping("/source-page/{source_page_id}")
    public List<PreferenceRuleDto> getRulesBySourcePageId(@PathVariable String source_page_id) {
        return ruleService.getRulesBySourcePageId(source_page_id);
    }

    /** GET /preference/rule/source-option/{source_option_id} */
    @GetMapping("/source-option/{source_option_id}")
    public List<PreferenceRuleDto> getRulesBySourceOptionId(@PathVariable String source_option_id) {
        return ruleService.getRulesBySourceOptionId(source_option_id);
    }

    /** GET /preference/rule/target-page/{target_page_id} */
    @GetMapping("/target-page/{target_page_id}")
    public List<PreferenceRuleDto> getRulesByTargetPageId(@PathVariable String target_page_id) {
        return ruleService.getRulesByTargetPageId(target_page_id);
    }
}
