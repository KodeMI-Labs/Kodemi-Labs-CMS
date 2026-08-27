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
@DynamoDBTable(tableName = "FAQ")
public class FAQ {
    @DynamoDBHashKey(attributeName = "faq_Id")
    private String faq_id;
    @DynamoDBAttribute(attributeName = "question")
    private String question;
    @DynamoDBAttribute(attributeName = "answer")
    private String answer;
    @DynamoDBAttribute(attributeName ="category")
    private String category;
    @DynamoDBAttribute(attributeName ="status")
    private String status;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
