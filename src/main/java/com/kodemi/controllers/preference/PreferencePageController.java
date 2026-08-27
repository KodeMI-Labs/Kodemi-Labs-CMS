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

import com.kodemi.dto.reviewsandratings.preference.PreferencePageDto;
import com.kodemi.service.preference.PreferencePageService;

import lombok.RequiredArgsConstructor;

/**
 * Preference Page endpoints (Sub Admin).
 *
 * URL summary:
 *   POST   /preference/page/create                              – create page
 *   PUT    /preference/page/update/{page_id}                   – update page
 *   DELETE /preference/page/delete/{page_id}                   – delete page
 *   PUT    /preference/page/deactivate/{page_id}               – soft-disable
 *   PUT    /preference/page/activate/{page_id}                 – re-enable
 *   GET    /preference/page/{page_id}                          – get by id
 *   GET    /preference/page/journey/{journey_id}               – all pages for a journey
 *   GET    /preference/page/journey/{journey_id}/active        – active pages only
 */
@RestController
@RequestMapping("/preference/page")
@RequiredArgsConstructor
public class PreferencePageController {

    private final PreferencePageService pageService;

    // ── Sub Admin CRUD ────────────────────────────────────────────────────────

    /** POST /preference/page/create */
    @PostMapping("/create")
    public PreferencePageDto createPage(@RequestBody PreferencePageDto dto) {
        return pageService.createPage(dto);
    }

    /** PUT /preference/page/update/{page_id} */
    @PutMapping("/update/{page_id}")
    public PreferencePageDto updatePage(
            @PathVariable String page_id,
            @RequestBody PreferencePageDto dto) {
        return pageService.updatePage(page_id, dto);
    }

    /** DELETE /preference/page/delete/{page_id} */
    @DeleteMapping("/delete/{page_id}")
    public String deletePage(@PathVariable String page_id) {
        return pageService.deletePage(page_id);
    }

    // ── Visibility toggle ─────────────────────────────────────────────────────

    /** PUT /preference/page/deactivate/{page_id} */
    @PutMapping("/deactivate/{page_id}")
    public String deactivatePage(@PathVariable String page_id) {
        return pageService.deactivatePage(page_id);
    }

    /** PUT /preference/page/activate/{page_id} */
    @PutMapping("/activate/{page_id}")
    public String activatePage(@PathVariable String page_id) {
        return pageService.activatePage(page_id);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    /** GET /preference/page/{page_id} */
    @GetMapping("/{page_id}")
    public PreferencePageDto getPageById(@PathVariable String page_id) {
        return pageService.getPageById(page_id);
    }

    /** GET /preference/page/journey/{journey_id} – all pages (active + inactive) */
    @GetMapping("/journey/{journey_id}")
    public List<PreferencePageDto> getPagesByJourneyId(@PathVariable String journey_id) {
        return pageService.getPagesByJourneyId(journey_id);
    }

    /** GET /preference/page/journey/{journey_id}/active – active pages only */
    @GetMapping("/journey/{journey_id}/active")
    public List<PreferencePageDto> getActivePagesByJourneyId(@PathVariable String journey_id) {
        return pageService.getActivePagesByJourneyId(journey_id);
    }
}
