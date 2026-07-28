package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.SEODto;
import com.ContentManagementSystem.CMS.model.SEO;

import java.util.List;

public interface SEOService {
    SEO createSEO(SEODto seoDto);
    String createSEO(SEO seo);
    SEODto getSEOId(String seo_id);
    List<SEODto> getAllSeo();
    String update(String seo_id,SEO seo);
    String deleteSEO(String seo_id);
}
