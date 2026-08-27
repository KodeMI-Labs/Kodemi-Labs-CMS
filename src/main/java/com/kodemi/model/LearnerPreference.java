package com.kodemi.model;

import java.time.LocalDateTime;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.kodemi.config.LocalDateTimeConverter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "LearnerPreference")
public class LearnerPreference {

    @DynamoDBHashKey(attributeName = "learner_id")
    private String learner_id;

    @DynamoDBAttribute(attributeName = "journey_id")
    private String journey_id;

    @DynamoDBAttribute(attributeName = "journey_version")
    private Integer journey_version;

    @DynamoDBAttribute(attributeName = "answers")
    private Map<String, String> answers;

    @DynamoDBAttribute(attributeName = "completed")
    private Boolean completed;

    @DynamoDBAttribute(attributeName = "completed_pages")
    private Integer completed_pages;

    @DynamoDBAttribute(attributeName = "skipped")
    private Boolean skipped;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "started_at")
    private LocalDateTime started_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "completed_at")
    private LocalDateTime completed_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
