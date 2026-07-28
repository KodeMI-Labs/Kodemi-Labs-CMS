package com.ContentManagementSystem.CMS.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SEODto {
    private String seo_id;
    private String meta_title;
    private String meta_description;
    private List<String> keywords;
    private String canonical_url;
    private String robots_tag;
    private String og_title;
    private String og_description;
    private String og_image;
}
