package com.kodemi.dto.reviewsandratings.preference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PreferenceJourneyVersionDto {

    private String version_id;
    private String journey_id;
    private Integer version;
    private Boolean active;
    private LocalDateTime published_at;
    private String remarks;
    private LocalDateTime created_at;
}
