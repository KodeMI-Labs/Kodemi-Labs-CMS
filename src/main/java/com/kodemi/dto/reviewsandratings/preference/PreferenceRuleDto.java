package com.kodemi.dto.reviewsandratings.preference;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PreferenceRuleDto {

    private String rule_id;
    private String source_page_id;
    private String source_option_id;
    private String target_page_id;
    private String rule_type;
    private Boolean active;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
