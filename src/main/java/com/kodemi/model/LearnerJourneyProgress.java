package com.kodemi.model;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.kodemi.config.LocalDateTimeConverter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "LearnerJourneyProgress")
public class LearnerJourneyProgress {

    @DynamoDBHashKey(attributeName = "progress_id")
    private String progress_id;

    @DynamoDBAttribute(attributeName = "learner_id")
    private String learner_id;

    @DynamoDBAttribute(attributeName = "journey_id")
    private String journey_id;

    @DynamoDBAttribute(attributeName = "current_page_id")
    private String current_page_id;

    @DynamoDBAttribute(attributeName = "current_page_order")
    private Integer current_page_order;

    @DynamoDBAttribute(attributeName = "total_pages")
    private Integer total_pages;

    @DynamoDBAttribute(attributeName = "completed_pages")
    private Integer completed_pages;

    @DynamoDBAttribute(attributeName = "completed")
    private Boolean completed;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "started_at")
    private LocalDateTime started_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
