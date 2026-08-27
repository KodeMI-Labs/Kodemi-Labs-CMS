package com.kodemi.model.reviewsandratings;


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
@DynamoDBTable(tableName = "InstructorReview")
public class InstructorReview {
    @DynamoDBHashKey(attributeName = "review_id")
    private String review_id;
    @DynamoDBAttribute(attributeName = "instructor_id")
    private String instructor;
    @DynamoDBAttribute(attributeName = "learner_id")
    private String learner_id;
    @DynamoDBAttribute(attributeName ="learner_name")
    private String learner_name;
    @DynamoDBAttribute(attributeName = "rating")
    private Integer rating;
    @DynamoDBAttribute(attributeName = "comment")
    private String comment;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "review_at")
    private LocalDateTime reviewed_at;
}