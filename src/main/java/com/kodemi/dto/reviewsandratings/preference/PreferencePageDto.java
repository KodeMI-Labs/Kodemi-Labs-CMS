package com.kodemi.dto.reviewsandratings.preference;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PreferencePageDto {

    private String page_id;
    private String journey_id;
    private Integer page_order;
    private String title;
    private String subtitle;
    private String description;
    private String field_type;
    private String selection_type;
    private Boolean searchable;
    private Boolean mandatory;
    private Boolean active;
    private String next_page_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
