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

import com.kodemi.dto.BannerDto;
import com.kodemi.model.Banner;
import com.kodemi.service.BannerService;

import lombok.RequiredArgsConstructor;

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