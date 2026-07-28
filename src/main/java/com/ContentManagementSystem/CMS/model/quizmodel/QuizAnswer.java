package com.ContentManagementSystem.CMS.model.quizmodel;

import com.ContentManagementSystem.CMS.config.LocalDateTimeConverter;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@DynamoDBTable(tableName = "QuizAnswer")
public class QuizAnswer {

    @DynamoDBHashKey(attributeName = "answer_id")
    private String answer_id;
    @DynamoDBAttribute(attributeName = "attempt_id")
    private String attempt_id;
    @DynamoDBAttribute(attributeName = "question_id")
    private String question_id;
    @DynamoDBAttribute(attributeName = "selectedOption_id")
    private String selectedOption_id;
    @DynamoDBAttribute(attributeName = "correct")
    private Boolean correct;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "answeredAt")
    private LocalDateTime answeredAt;
}