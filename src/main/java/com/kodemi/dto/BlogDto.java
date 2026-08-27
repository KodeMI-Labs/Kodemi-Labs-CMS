package com.kodemi.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

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
    private String rejection_reason;
    private String reviewed_by;
    private String reviewed_at;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
