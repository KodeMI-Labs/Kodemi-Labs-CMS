package com.ContentManagementSystem.CMS.service.impl;
import com.ContentManagementSystem.CMS.dto.BannerDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Banner;
import com.ContentManagementSystem.CMS.repository.BannerRepository;
import com.ContentManagementSystem.CMS.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    @Override
    public Banner createBanner(BannerDto bannerDto) {
        Banner banner = new Banner();
        BeanUtils.copyProperties(bannerDto, banner);
        banner.setBanner_Id(UUID.randomUUID().toString());
        banner.setCreated_at(LocalDateTime.now());
        banner.setUpdated_at(LocalDateTime.now());
        bannerRepository.save(banner);
        return banner;
    }
    @Override
    public String createBanner(Banner banner) {
        banner.setBanner_Id(UUID.randomUUID().toString());
        banner.setCreated_at(LocalDateTime.now());
        banner.setUpdated_at(LocalDateTime.now());
        bannerRepository.save(banner);
        return "Banner Created Successfully";
    }
    @Override
    public BannerDto getBannerId(String banner_Id) {
        Banner banner = bannerRepository.findById(banner_Id);
        if (banner == null) {
            throw new ResourceNotFoundException("Banner not found with id: " + banner_Id);
        }
        BannerDto bannerDto = new BannerDto();
        BeanUtils.copyProperties(banner, bannerDto);
        return bannerDto;
    }
    @Override
    public List<BannerDto> getAllBanner() {
        List<Banner> banners = bannerRepository.findAll();
        List<BannerDto> bannerDtoList = new ArrayList<>();
        for (Banner banner : banners) {
            BannerDto bannerDto = new BannerDto();
            BeanUtils.copyProperties(banner, bannerDto);
            bannerDtoList.add(bannerDto);
        }
        return bannerDtoList;
    }
    @Override
    public String updateBanner(String banner_Id, Banner banner) {
        Banner existingBanner = bannerRepository.findById(banner_Id);
        if (existingBanner == null) {
            throw new ResourceNotFoundException("Banner not found with id: " + banner_Id);
        }
        existingBanner.setTitle(banner.getTitle());
        existingBanner.setSubtitle(banner.getSubtitle());
        existingBanner.setImage_url(banner.getImage_url());
        existingBanner.setRedirect_url(banner.getRedirect_url());
        existingBanner.setPriority(banner.getPriority());
        existingBanner.setStatus(banner.getStatus());
        existingBanner.setUpdated_at(LocalDateTime.now());
        bannerRepository.save(existingBanner);
        return "Banner Updated Successfully";
    }
    @Override
    public String deleteBanner(String banner_Id) {
        Banner banner = bannerRepository.findById(banner_Id);
        if (banner == null) {
            throw new ResourceNotFoundException("Banner not found with id: " + banner_Id);
        }
        bannerRepository.delete(banner_Id);
        return "Banner Deleted Successfully";
    }
}