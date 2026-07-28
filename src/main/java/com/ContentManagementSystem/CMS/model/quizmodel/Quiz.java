package com.ContentManagementSystem.CMS.model.quizmodel;

import com.ContentManagementSystem.CMS.config.LocalDateTimeConverter;
import com.ContentManagementSystem.CMS.enums.QuizStatus;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@DynamoDBTable(tableName ="Quiz")
public class Quiz {
    @DynamoDBHashKey(attributeName ="quiz_id")
    private String quiz_id;
    @DynamoDBAttribute(attributeName = "title")
    private String title;
    @DynamoDBAttribute(attributeName = "description")
    private String description;
    @DynamoDBAttribute(attributeName = "category")
    private String category;
    @DynamoDBAttribute(attributeName = "trainer_id")
    private String trainer_id;
    @DynamoDBAttribute(attributeName = "durationMinutes")
    private Integer durationMinutes;
    @DynamoDBAttribute(attributeName = "totalQuestion" )
    private Integer totalQuestion;
    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = "status")
    private QuizStatus status;
    @DynamoDBAttribute(attributeName = "averageRating")
    private Double averageRating;
    @DynamoDBAttribute(attributeName = "totalAttempts")
    private Integer totalAttempts;
    @DynamoDBAttribute(attributeName = "totalMarks")
    private Integer totalMarks;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "quizDate")
    private LocalDateTime quizDate;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "createdAt")
    private LocalDateTime createdAt;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updateAt")
    private LocalDateTime updateAt;
}
