package com.ContentManagementSystem.CMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestimonialDto {

    private String testimonial_id;
    private String user_id;
    private String user_name;
    private String user_profile_image;
    private String user_designation;
    private String user_company;
    private String content;
    private Integer rating;
    private String status;
    private Boolean featured;
    private String created_at;
    private String updated_at;
}
