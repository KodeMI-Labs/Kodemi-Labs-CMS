package com.kodemi.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.SEODto;
import com.kodemi.model.SEO;
import com.kodemi.repository.SEORepository;
import com.kodemi.service.SEOService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SEOServiceImpl implements SEOService {
    private final SEORepository seoRepository;
    @Override
    public SEO createSEO(SEODto seoDto) {
        SEO seo = new SEO();
        BeanUtils.copyProperties(seoDto, seo);
        seo.setSeo_id(UUID.randomUUID().toString());
        seo.setCreated_at(LocalDateTime.now());
        seo.setUpdated_at(LocalDateTime.now());
        seoRepository.save(seo);
        return seo;
    }
    @Override
    public String createSEO(SEO seo) {
        seo.setSeo_id(UUID.randomUUID().toString());
        seo.setCreated_at(LocalDateTime.now());
        seo.setUpdated_at(LocalDateTime.now());
        seoRepository.save(seo);
        return "SEO Created Successfully";
    }
    @Override
    public SEODto getSEOId(String seo_id) {
        SEO seo = seoRepository.findById(seo_id);
        if (seo == null) {
            throw new ResourceNotFoundException("SEO not found with id: " + seo_id);
        }
        SEODto seoDto = new SEODto();
        BeanUtils.copyProperties(seo, seoDto);
        return seoDto;
    }
    @Override
    public List<SEODto> getAllSeo() {
        List<SEO> seoList = seoRepository.findAll();
        List<SEODto> seoDtoList = new ArrayList<>();
        for (SEO seo : seoList) {
            SEODto seoDto = new SEODto();
            BeanUtils.copyProperties(seo, seoDto);
            seoDtoList.add(seoDto);
        }
        return seoDtoList;
    }
    @Override
    public String update(String seo_id, SEO seo) {
        SEO existingSEO = seoRepository.findById(seo_id);
        if (existingSEO == null) {
            throw new ResourceNotFoundException("SEO not found with id: " + seo_id);
        }
        existingSEO.setMeta_title(seo.getMeta_title());
        existingSEO.setMeta_description(seo.getMeta_description());
        existingSEO.setKeywords(seo.getKeywords());
        existingSEO.setCanonical_url(seo.getCanonical_url());
        existingSEO.setRobots_tag(seo.getRobots_tag());
        existingSEO.setOg_title(seo.getOg_title());
        existingSEO.setOg_description(seo.getOg_description());
        existingSEO.setOg_image(seo.getOg_image());
        existingSEO.setUpdated_at(LocalDateTime.now());
        seoRepository.save(existingSEO);
        return "SEO Updated Successfully";
    }
    @Override
    public String deleteSEO(String seo_id) {
        SEO seo = seoRepository.findById(seo_id);
        if (seo == null) {
            throw new ResourceNotFoundException("SEO not found with id: " + seo_id);
        }
        seoRepository.delete(seo_id);
        return "SEO Deleted Successfully";
    }
}