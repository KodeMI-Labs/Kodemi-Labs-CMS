package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.BannerDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Banner;
import com.ContentManagementSystem.CMS.repository.BannerRepository;
import com.ContentManagementSystem.CMS.service.impl.BannerServiceImpl;
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
class BannerServiceImplTest {

    @Mock
    private BannerRepository bannerRepository;

    @InjectMocks
    private BannerServiceImpl bannerService;

    private Banner banner;

    @BeforeEach
    void setUp() {
        banner = new Banner();
        banner.setBanner_Id("b1");
        banner.setTitle("Test Banner");
        banner.setStatus("ACTIVE");
    }

    @Test
    void createBanner_withDto_returnsBanner() {
        BannerDto dto = new BannerDto();
        dto.setBanner_Id("b1");
        dto.setTitle("Test Banner");
        when(bannerRepository.save(any(Banner.class))).thenAnswer(i -> i.getArgument(0));

        Banner result = bannerService.createBanner(dto);

        assertNotNull(result);
        assertEquals("Test Banner", result.getTitle());
        verify(bannerRepository).save(any(Banner.class));
    }

    @Test
    void createBanner_withModel_returnsSuccessMessage() {
        when(bannerRepository.save(banner)).thenReturn(banner);

        String result = bannerService.createBanner(banner);

        assertEquals("Banner Created Successfully", result);
    }

    @Test
    void getBannerId_found_returnsDto() {
        when(bannerRepository.findById("b1")).thenReturn(banner);

        BannerDto result = bannerService.getBannerId("b1");

        assertNotNull(result);
        assertEquals("Test Banner", result.getTitle());
    }

    @Test
    void getBannerId_notFound_throwsException() {
        when(bannerRepository.findById("b1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> bannerService.getBannerId("b1"));
    }

    @Test
    void getAllBanner_returnsList() {
        when(bannerRepository.findAll()).thenReturn(List.of(banner));

        List<BannerDto> result = bannerService.getAllBanner();

        assertEquals(1, result.size());
    }

    @Test
    void updateBanner_found_returnsSuccessMessage() {
        when(bannerRepository.findById("b1")).thenReturn(banner);
        when(bannerRepository.save(any())).thenReturn(banner);

        String result = bannerService.updateBanner("b1", banner);

        assertEquals("Banner Updated Successfully", result);
    }

    @Test
    void updateBanner_notFound_throwsException() {
        when(bannerRepository.findById("b1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> bannerService.updateBanner("b1", banner));
    }

    @Test
    void deleteBanner_found_returnsSuccessMessage() {
        when(bannerRepository.findById("b1")).thenReturn(banner);

        String result = bannerService.deleteBanner("b1");

        assertEquals("Banner Deleted Successfully", result);
        verify(bannerRepository).delete("b1");
    }

    @Test
    void deleteBanner_notFound_throwsException() {
        when(bannerRepository.findById("b1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> bannerService.deleteBanner("b1"));
    }
}
