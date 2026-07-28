package com.ContentManagementSystem.CMS.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BlogDto {
    private String blog_Id;
    private String title;
    private String content;
    private String author;
    private String category;
    private List<String> tags;
    private List<String> featured_images;
    private String status;
    private String thumbnail;
    private String trainer_id;
}
