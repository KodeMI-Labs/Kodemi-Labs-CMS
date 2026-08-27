package com.kodemi.controllers.preference;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kodemi.dto.reviewsandratings.preference.PreferenceOptionDto;
import com.kodemi.service.S3Service;
import com.kodemi.service.preference.PreferenceOptionService;

import lombok.RequiredArgsConstructor;

/**
 * Preference Option endpoints (Sub Admin).
 *
 * The icon upload endpoints mirror the blog thumbnail pattern:
 *   1. Sub Admin uploads the icon image → receives back a URL
 *   2. Sub Admin sends that URL in the create/update option body as icon_url
 *
 * S3 folder layout:
 *   preference-icons/   → small circular icons shown on ICON_GRID pages
 *                          (Business, Designing, IT & Software, Accounting …)
 *   preference-images/  → larger image-based option cards (optional variant)
 *
 * URL summary:
 *   POST   /preference/option/upload-icon                      – upload icon → returns iconUrl
 *   POST   /preference/option/upload-image                     – upload large image → returns imageUrl
 *   POST   /preference/option/create                           – create option
 *   PUT    /preference/option/update/{option_id}               – update option
 *   DELETE /preference/option/delete/{option_id}               – delete option
 *   PUT    /preference/option/deactivate/{option_id}           – soft-disable
 *   PUT    /preference/option/activate/{option_id}             – re-enable
 *   GET    /preference/option/{option_id}                      – get by id
 *   GET    /preference/option/page/{page_id}                   – all options for a page
 *   GET    /preference/option/page/{page_id}/active            – active options only
 *   GET    /preference/option/children/{parent_option_id}      – Phase 3 child options
 */
@RestController
@RequestMapping("/preference/option")
@RequiredArgsConstructor
public class PreferenceOptionController {

    private final PreferenceOptionService optionService;
    private final S3Service               s3Service;

    // ── S3 Icon / Image Upload ────────────────────────────────────────────────

    /**
     * POST /preference/option/upload-icon
     * Uploads a small circular icon for an option (used on ICON_GRID pages).
     *
     * Usage:
     *   multipart/form-data, field name: file
     *
     * Returns:
     *   { "iconUrl": "https://bucket.s3.region.amazonaws.com/preference-icons/uuid.png" }
     *
     * Workflow:
     *   1. Call this endpoint → copy the returned iconUrl
     *   2. Pass iconUrl in the create/update option request body
     *
     * Example options from the UI mockup that need icons:
     *   Business | Designing | IT & Software | Accounting | Personal Development | Data & Analyst | Teaching & Academics
     */
    @PostMapping(value = "/upload-icon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadIcon(@RequestParam("file") MultipartFile file) {
        String url = s3Service.uploadFile(file, "preference-icons");
        return Map.of("iconUrl", url);
    }

    /**
     * POST /preference/option/upload-image
     * Uploads a larger image card for an option (optional variant for image-based pages).
     *
     * Usage:
     *   multipart/form-data, field name: file
     *
     * Returns:
     *   { "imageUrl": "https://bucket.s3.region.amazonaws.com/preference-images/uuid.jpg" }
     *
     * Workflow:
     *   1. Call this endpoint → copy the returned imageUrl
     *   2. Pass imageUrl in the create/update option request body
     */
    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = s3Service.uploadFile(file, "preference-images");
        return Map.of("imageUrl", url);
    }

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    /**
     * POST /preference/option/create
     *
     * Example body for a Profession page option:
     * {
     *   "page_id":       "page-uuid",
     *   "display_name":  "Designing",
     *   "value":         "DESIGNING",
     *   "icon_url":      "https://bucket.s3.region.amazonaws.com/preference-icons/uuid.png",
     *   "display_order": 2,
     *   "active":        true
     * }
     */
    @PostMapping("/create")
    public PreferenceOptionDto createOption(@RequestBody PreferenceOptionDto dto) {
        return optionService.createOption(dto);
    }

    /**
     * PUT /preference/option/update/{option_id}
     * Use this to swap out an icon: upload new icon first, then pass the new iconUrl here.
     */
    @PutMapping("/update/{option_id}")
    public PreferenceOptionDto updateOption(
            @PathVariable String option_id,
            @RequestBody PreferenceOptionDto dto) {
        return optionService.updateOption(option_id, dto);
    }

    /** DELETE /preference/option/delete/{option_id} */
    @DeleteMapping("/delete/{option_id}")
    public String deleteOption(@PathVariable String option_id) {
        return optionService.deleteOption(option_id);
    }

    // ── Visibility toggle ─────────────────────────────────────────────────────

    /** PUT /preference/option/deactivate/{option_id} */
    @PutMapping("/deactivate/{option_id}")
    public String deactivateOption(@PathVariable String option_id) {
        return optionService.deactivateOption(option_id);
    }

    /** PUT /preference/option/activate/{option_id} */
    @PutMapping("/activate/{option_id}")
    public String activateOption(@PathVariable String option_id) {
        return optionService.activateOption(option_id);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    /** GET /preference/option/{option_id} */
    @GetMapping("/{option_id}")
    public PreferenceOptionDto getOptionById(@PathVariable String option_id) {
        return optionService.getOptionById(option_id);
    }

    /** GET /preference/option/page/{page_id} – all options (active + inactive) */
    @GetMapping("/page/{page_id}")
    public List<PreferenceOptionDto> getOptionsByPageId(@PathVariable String page_id) {
        return optionService.getOptionsByPageId(page_id);
    }

    /** GET /preference/option/page/{page_id}/active – active options only */
    @GetMapping("/page/{page_id}/active")
    public List<PreferenceOptionDto> getActiveOptionsByPageId(@PathVariable String page_id) {
        return optionService.getActiveOptionsByPageId(page_id);
    }

    /**
     * GET /preference/option/children/{parent_option_id}
     * Returns child options of a given parent (Phase 3 conditional options).
     * e.g. parent = "option-designing" → UI UX, Motion Graphics, Game Design …
     */
    @GetMapping("/children/{parent_option_id}")
    public List<PreferenceOptionDto> getChildOptions(@PathVariable String parent_option_id) {
        return optionService.getChildOptions(parent_option_id);
    }
}
