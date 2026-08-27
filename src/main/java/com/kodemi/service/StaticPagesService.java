package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.StaticPagesDto;
import com.kodemi.model.StaticPages;

public interface StaticPagesService {
    StaticPages createStaticPages(StaticPagesDto staticPagesDto);
    String createStaticPages(StaticPages staticPages);
    StaticPagesDto getAllStaticPages(String page_id);
    List<StaticPagesDto> getAllStaticPages();
    String updateStaticPages(String page_id,StaticPages staticPages);
    String deleteStaticPages(String page_id);
}
