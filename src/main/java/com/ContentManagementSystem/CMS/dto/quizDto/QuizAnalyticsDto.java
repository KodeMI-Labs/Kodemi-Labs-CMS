package com.ContentManagementSystem.CMS.dto.quizDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizAnalyticsDto {

    private String analytics_id;
    private String quiz_id;
    private String quiz_title;

    // Summary
    private Integer total_students;
    private Integer attempted_count;
    private Integer not_attempted_count;
    private Double avg_score_percent;
    private Double highest_score_percent;

    // Overall performance
    private Double overall_performance_percent;

    // Participation
    private Integer completed_count;
    private Integer in_progress_count;
    private Integer not_started_count;

    // Score distribution
    private Double range_90_100_percent;
    private Double range_80_89_percent;
    private Double range_70_79_percent;
    private Double range_60_69_percent;
    private Double range_50_59_percent;
}
