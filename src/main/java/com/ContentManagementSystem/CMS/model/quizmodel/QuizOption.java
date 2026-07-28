package com.ContentManagementSystem.CMS.model.quizmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "QuizOption")
public class QuizOption {
        @DynamoDBHashKey(attributeName = "option_id")
        private String optionId;
        @DynamoDBAttribute(attributeName = "questionId")
        private String questionId;
        @DynamoDBAttribute(attributeName = "optionText")
        private String optionText;
        @DynamoDBAttribute(attributeName = "correct")
        private Boolean correct;
    }
