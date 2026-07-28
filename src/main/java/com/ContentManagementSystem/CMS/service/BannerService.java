package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.BannerDto;
import com.ContentManagementSystem.CMS.model.Banner;

import java.util.List;

public interface BannerService {
    Banner createBanner(BannerDto bannerDto);
    String createBanner(Banner banner);
    BannerDto getBannerId(String banner_Id);
    List<BannerDto> getAllBanner();
    String updateBanner(String banner_Id,Banner banner);
    String deleteBanner(String banner_Id);
}
