package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.SEODto;
import com.ContentManagementSystem.CMS.model.SEO;
import com.ContentManagementSystem.CMS.service.SEOService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seo")
@RequiredArgsConstructor
public class SEOController {

    private final SEOService seoService;
    @PostMapping("/create-dto")
    public SEO createSEODto(
            @RequestBody SEODto seoDto) {
        return seoService.createSEO(seoDto);
    }
    @PostMapping("/create")
    public String createSEO(
            @RequestBody SEO seo) {
        return seoService.createSEO(seo);
    }
    @GetMapping("/{seo_id}")
    public SEODto getSEOById(
            @PathVariable String seo_id) {
        return seoService.getSEOId(seo_id);
    }
    @GetMapping("/all")
    public List<SEODto> getAllSEO() {
        return seoService.getAllSeo();
    }
    @PutMapping("/update/{seo_id}")
    public String updateSEO(
            @PathVariable String seo_id,
            @RequestBody SEO seo) {

        return seoService.update(
                seo_id,
                seo
        );
    }
    @DeleteMapping("/delete/{seo_id}")
    public String deleteSEO(
            @PathVariable String seo_id) {
        return seoService.deleteSEO(seo_id);
    }
}