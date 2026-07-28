package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.StaticPagesDto;
import com.ContentManagementSystem.CMS.model.StaticPages;

import java.util.List;

public interface StaticPagesService {
    StaticPages createStaticPages(StaticPagesDto staticPagesDto);
    String createStaticPages(StaticPages staticPages);
    StaticPagesDto getAllStaticPages(String page_id);
    List<StaticPagesDto> getAllStaticPages();
    String updateStaticPages(String page_id,StaticPages staticPages);
    String deleteStaticPages(String page_id);
}
