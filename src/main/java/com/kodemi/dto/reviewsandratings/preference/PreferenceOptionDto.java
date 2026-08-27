package com.kodemi.dto.reviewsandratings.preference;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PreferenceOptionDto {

    private String option_id;
    private String page_id;
    private String display_name;
    private String value;
    private String icon_url;
    private String image_url;
    private Integer display_order;
    private Boolean active;
    private String parent_option_id;
    private String next_page_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
