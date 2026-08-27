package com.kodemi.service;


import java.util.List;

import com.kodemi.dto.BannerDto;
import com.kodemi.model.Banner;

public interface BannerService {
    Banner createBanner(BannerDto bannerDto);
    String createBanner(Banner banner);
    BannerDto getBannerId(String banner_Id);
    List<BannerDto> getAllBanner();
    String updateBanner(String banner_Id,Banner banner);
    String deleteBanner(String banner_Id);
}
