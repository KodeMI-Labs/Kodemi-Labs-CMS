package com.ContentManagementSystem.CMS.model.quizmodel;

import com.ContentManagementSystem.CMS.config.LocalDateTimeConverter;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@DynamoDBTable(tableName = "QuizAnalytics")
public class QuizAnalytics {

    @DynamoDBHashKey(attributeName = "analytics_id")
    private String analytics_id;
    @DynamoDBAttribute(attributeName = "quiz_id")
    private String quiz_id;
    @DynamoDBAttribute(attributeName = "quiz_title")
    private String quiz_title;
    @DynamoDBAttribute(attributeName = "total_students")
    private Integer total_students;
    @DynamoDBAttribute(attributeName = "attempted_count")
    private Integer attempted_count;
    @DynamoDBAttribute(attributeName = "not_attempted_count")
    private Integer not_attempted_count;
    @DynamoDBAttribute(attributeName = "avg_score_percent")
    private Double avg_score_percent;
    @DynamoDBAttribute(attributeName = "highest_score_percent")
    private Double highest_score_percent;
    @DynamoDBAttribute(attributeName = "overall_performance_percent")
    private Double overall_performance_percent;
    @DynamoDBAttribute(attributeName = "completed_count")
    private Integer completed_count;
    @DynamoDBAttribute(attributeName = "in_progress_count")
    private Integer in_progress_count;
    @DynamoDBAttribute(attributeName = "not_started_count")
    private Integer not_started_count;
    @DynamoDBAttribute(attributeName = "range_90_100_percent")
    private Double range_90_100_percent;
    @DynamoDBAttribute(attributeName = "range_80_89_percent")
    private Double range_80_89_percent;
    @DynamoDBAttribute(attributeName = "range_70_79_percent")
    private Double range_70_79_percent;
    @DynamoDBAttribute(attributeName = "range_60_69_percent")
    private Double range_60_69_percent;

    @DynamoDBAttribute(attributeName = "range_50_59_percent")
    private Double range_50_59_percent;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
