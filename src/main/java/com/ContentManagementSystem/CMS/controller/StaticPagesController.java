package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.StaticPagesDto;
import com.ContentManagementSystem.CMS.model.StaticPages;
import com.ContentManagementSystem.CMS.service.StaticPagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/static-pages")
@RequiredArgsConstructor
public class StaticPagesController {

    private final StaticPagesService staticPagesService;
    @PostMapping("/create-dto")
    public StaticPages createStaticPagesDto(
            @RequestBody StaticPagesDto staticPagesDto) {
        return staticPagesService.createStaticPages(
                staticPagesDto
        );
    }
    @PostMapping("/create")
    public String createStaticPages(
            @RequestBody StaticPages staticPages) {
        return staticPagesService.createStaticPages(
                staticPages
        );
    }
    @GetMapping("/{page_id}")
    public StaticPagesDto getStaticPageById(
            @PathVariable String page_id) {
        return staticPagesService.getAllStaticPages(
                page_id
        );
    }
    @GetMapping("/all")
    public List<StaticPagesDto> getAllStaticPages() {

        return staticPagesService.getAllStaticPages();
    }
    @PutMapping("/update/{page_id}")
    public String updateStaticPages(
            @PathVariable String page_id,
            @RequestBody StaticPages staticPages) {

        return staticPagesService.updateStaticPages(
                page_id,
                staticPages
        );
    }
    @DeleteMapping("/delete/{page_id}")
    public String deleteStaticPages(
            @PathVariable String page_id) {

        return staticPagesService.deleteStaticPages(
                page_id
        );
    }
}