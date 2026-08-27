
package com.kodemi.service.impl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.StaticPagesDto;
import com.kodemi.model.StaticPages;
import com.kodemi.repository.StaticPagesRepository;
import com.kodemi.service.StaticPagesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaticPagesServiceImpl implements StaticPagesService {

    private final StaticPagesRepository staticPagesRepository;
    @Override
    public StaticPages createStaticPages(StaticPagesDto staticPagesDto) {
        StaticPages staticPages = new StaticPages();
        BeanUtils.copyProperties(staticPagesDto, staticPages);
        staticPages.setPage_id(UUID.randomUUID().toString());
        staticPages.setCreated_at(LocalDateTime.now());
        staticPages.setUpdated_at(LocalDateTime.now());
        staticPagesRepository.save(staticPages);
        return staticPages;
    }
    @Override
    public String createStaticPages(StaticPages staticPages) {
        staticPages.setPage_id(UUID.randomUUID().toString());
        staticPages.setCreated_at(LocalDateTime.now());
        staticPages.setUpdated_at(LocalDateTime.now());
        staticPagesRepository.save(staticPages);
        return "Static Page Created Successfully";
    }
    @Override
    public StaticPagesDto getAllStaticPages(String page_id) {
        StaticPages staticPages = staticPagesRepository.findById(page_id);
        if (staticPages == null) {
            throw new ResourceNotFoundException("Static page not found with id: " + page_id);
        }
        StaticPagesDto staticPagesDto = new StaticPagesDto();
        BeanUtils.copyProperties(staticPages, staticPagesDto);
        return staticPagesDto;
    }
    @Override
    public List<StaticPagesDto> getAllStaticPages() {
        List<StaticPages> staticPagesList =
                staticPagesRepository.findAll();
        List<StaticPagesDto> staticPagesDtoList =
                new ArrayList<>();
        for (StaticPages staticPages : staticPagesList) {
            StaticPagesDto staticPagesDto =
                    new StaticPagesDto();
            BeanUtils.copyProperties(staticPages,
                    staticPagesDto);
            staticPagesDtoList.add(staticPagesDto);
        }
        return staticPagesDtoList;
    }
    @Override
    public String updateStaticPages(String page_id,
                                    StaticPages staticPages) {
        StaticPages existingStaticPage =
                staticPagesRepository.findById(page_id);
        if (existingStaticPage == null) {
            throw new ResourceNotFoundException("Static page not found with id: " + page_id);
        }
        existingStaticPage.setTitle(staticPages.getTitle());
        existingStaticPage.setSlug(staticPages.getSlug());
        existingStaticPage.setContent(staticPages.getContent());
        existingStaticPage.setStatus(staticPages.getStatus());
        existingStaticPage.setUpdated_at(LocalDateTime.now());
        staticPagesRepository.save(existingStaticPage);
        return "Static Page Updated Successfully";
    }
    @Override
    public String deleteStaticPages(String page_id) {
        StaticPages staticPages =
                staticPagesRepository.findById(page_id);
        if (staticPages == null) {
            throw new ResourceNotFoundException("Static page not found with id: " + page_id);
        }
        staticPagesRepository.delete(page_id);
        return "Static Page Deleted Successfully";
    }
}