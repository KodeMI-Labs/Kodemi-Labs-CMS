package com.ContentManagementSystem.CMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FAQDto {
    private String faq_Id;
    private String question;
    private String answer;
    private String category;
    private String status;
}
