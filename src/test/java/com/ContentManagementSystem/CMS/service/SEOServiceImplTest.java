package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.SEODto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.SEO;
import com.ContentManagementSystem.CMS.repository.SEORepository;
import com.ContentManagementSystem.CMS.service.impl.SEOServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SEOServiceImplTest {

    @Mock
    private SEORepository seoRepository;

    @InjectMocks
    private SEOServiceImpl seoService;

    private SEO seo;

    @BeforeEach
    void setUp() {
        seo = new SEO();
        seo.setSeo_id("seo1");
        seo.setMeta_title("Test Title");
        seo.setMeta_description("Test Description");
        seo.setCanonical_url("http://example.com");
        seo.setRobots_tag("INDEX_FOLLOW");
    }

    @Test
    void createSEO_withDto_returnsSEO() {
        SEODto dto = new SEODto();
        dto.setSeo_id("seo1");
        dto.setMeta_title("Test Title");
        when(seoRepository.save(any(SEO.class))).thenAnswer(i -> i.getArgument(0));

        SEO result = seoService.createSEO(dto);

        assertNotNull(result);
        assertEquals("Test Title", result.getMeta_title());
    }

    @Test
    void createSEO_withModel_returnsSuccessMessage() {
        when(seoRepository.save(seo)).thenReturn(seo);

        String result = seoService.createSEO(seo);

        assertEquals("SEO Created Successfully", result);
    }

    @Test
    void getSEOId_found_returnsDto() {
        when(seoRepository.findById("seo1")).thenReturn(seo);

        SEODto result = seoService.getSEOId("seo1");

        assertNotNull(result);
        assertEquals("Test Title", result.getMeta_title());
    }

    @Test
    void getSEOId_notFound_throwsException() {
        when(seoRepository.findById("seo1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> seoService.getSEOId("seo1"));
    }

    @Test
    void getAllSeo_returnsList() {
        when(seoRepository.findAll()).thenReturn(List.of(seo));

        List<SEODto> result = seoService.getAllSeo();

        assertEquals(1, result.size());
    }

    @Test
    void update_found_returnsSuccessMessage() {
        when(seoRepository.findById("seo1")).thenReturn(seo);
        when(seoRepository.save(any())).thenReturn(seo);

        String result = seoService.update("seo1", seo);

        assertEquals("SEO Updated Successfully", result);
    }

    @Test
    void update_notFound_throwsException() {
        when(seoRepository.findById("seo1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> seoService.update("seo1", seo));
    }

    @Test
    void deleteSEO_found_returnsSuccessMessage() {
        when(seoRepository.findById("seo1")).thenReturn(seo);

        String result = seoService.deleteSEO("seo1");

        assertEquals("SEO Deleted Successfully", result);
        verify(seoRepository).delete("seo1");
    }

    @Test
    void deleteSEO_notFound_throwsException() {
        when(seoRepository.findById("seo1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> seoService.deleteSEO("seo1"));
    }
}
