package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.BannerDto;
import com.ContentManagementSystem.CMS.model.Banner;
import com.ContentManagementSystem.CMS.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;
    @PostMapping("/create-dto")
    public Banner createBannerDto(
            @RequestBody BannerDto bannerDto) {

        return bannerService.createBanner(bannerDto);
    }
    @PostMapping("/create")
    public String createBanner(
            @RequestBody Banner banner) {

        return bannerService.createBanner(banner);
    }
    @GetMapping("/{banner_Id}")
    public BannerDto getBannerById(
            @PathVariable String banner_Id) {
        return bannerService.getBannerId(banner_Id);
    }
    @GetMapping("/all")
    public List<BannerDto> getAllBanner() {
        return bannerService.getAllBanner();
    }
    @PutMapping("/update/{banner_Id}")
    public String updateBanner(
            @PathVariable String banner_Id,
            @RequestBody Banner banner) {
        return bannerService.updateBanner(
                banner_Id,
                banner
        );
    }
    @DeleteMapping("/delete/{banner_Id}")
    public String deleteBanner(
            @PathVariable String banner_Id) {
        return bannerService.deleteBanner(banner_Id);
    }
}