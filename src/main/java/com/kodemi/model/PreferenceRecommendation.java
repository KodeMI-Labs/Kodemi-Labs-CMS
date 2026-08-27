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
@DynamoDBTable(tableName = "PreferenceRecommendation")
public class PreferenceRecommendation {

    @DynamoDBHashKey(attributeName = "recommendation_id")
    private String recommendation_id;

    @DynamoDBAttribute(attributeName = "option_id")
    private String option_id;

    @DynamoDBAttribute(attributeName = "course_category_id")
    private String course_category_id;

    @DynamoDBAttribute(attributeName = "priority")
    private Integer priority;

    @DynamoDBAttribute(attributeName = "active")
    private Boolean active;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
