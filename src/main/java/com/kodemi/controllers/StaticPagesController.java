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

import com.kodemi.dto.StaticPagesDto;
import com.kodemi.model.StaticPages;
import com.kodemi.service.StaticPagesService;

import lombok.RequiredArgsConstructor;

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