package com.ContentManagementSystem.CMS.model.quizmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "QuizStudentResult")
public class QuizStudentResult {

    @DynamoDBHashKey(attributeName = "result_id")
    private String result_id;

    @DynamoDBAttribute(attributeName = "quiz_id")
    private String quiz_id;

    @DynamoDBAttribute(attributeName = "learner_id")
    private String learner_id;

    @DynamoDBAttribute(attributeName = "learner_name")
    private String learner_name;

    @DynamoDBAttribute(attributeName = "questions_attempted")
    private Integer questions_attempted;

    @DynamoDBAttribute(attributeName = "total_questions")
    private Integer total_questions;

    @DynamoDBAttribute(attributeName = "score")
    private Integer score;

    @DynamoDBAttribute(attributeName = "total_marks")
    private Integer total_marks;

    @DynamoDBAttribute(attributeName = "percent")
    private Double percent;

    @DynamoDBAttribute(attributeName = "time_taken_minutes")
    private Integer time_taken_minutes;

    @DynamoDBAttribute(attributeName = "status")
    private String status;
}
