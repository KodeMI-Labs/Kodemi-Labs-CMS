package com.kodemi.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.SEODto;
import com.kodemi.model.SEO;
import com.kodemi.service.SEOService;

import lombok.RequiredArgsConstructor;

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