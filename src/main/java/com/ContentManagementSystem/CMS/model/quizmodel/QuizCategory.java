package com.ContentManagementSystem.CMS.model.quizmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "QuizCategory")
public class QuizCategory {

    @DynamoDBHashKey(attributeName = "category_id")
    private String categoryId;
    @DynamoDBAttribute(attributeName = "categoryName")
    private String categoryName;
    @DynamoDBAttribute(attributeName = "description")
    private String description;
    @DynamoDBAttribute(attributeName = "active")
    private Boolean active;
}