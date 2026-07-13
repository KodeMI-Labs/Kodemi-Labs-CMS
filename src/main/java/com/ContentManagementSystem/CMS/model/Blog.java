package com.ContentManagementSystem.CMS.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.ContentManagementSystem.CMS.config.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@DynamoDBTable(tableName = "Blog")
public class Blog {

    @DynamoDBHashKey(attributeName ="blog_Id")
    private String blog_Id;

    @DynamoDBAttribute(attributeName="title")
    private String title;

    @DynamoDBAttribute(attributeName = "content")
    private String content;

    @DynamoDBAttribute(attributeName = "author")
    private String author;

    @DynamoDBAttribute(attributeName = "category")
    private String category;

    @DynamoDBAttribute(attributeName = "tags")
    private List<String> tags;

    @DynamoDBAttribute(attributeName = "featured_images")
    private List<String> featured_images;

    @DynamoDBAttribute(attributeName = "status")
    private String status;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}