package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.SEODto;
import com.kodemi.model.SEO;

public interface SEOService {
    SEO createSEO(SEODto seoDto);
    String createSEO(SEO seo);
    SEODto getSEOId(String seo_id);
    List<SEODto> getAllSeo();
    String update(String seo_id,SEO seo);
    String deleteSEO(String seo_id);
}
