package com.kodemi.dto.reviewsandratings.preference;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PreferenceJourneyDto {

    private String journey_id;
    private String journey_name;
    private String description;
    private Integer version;
    private String status;
    private Boolean active;
    private LocalDateTime publish_date;
    private String created_by;
    private String updated_by;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
