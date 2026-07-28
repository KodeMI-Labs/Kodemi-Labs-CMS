package com.ContentManagementSystem.CMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaticPagesDto {
    private String page_Id;
    private String title;
    private String slug;
    private String content;
    private String status;
}
