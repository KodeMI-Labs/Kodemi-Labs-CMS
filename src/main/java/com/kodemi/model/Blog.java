package com.kodemi.model;

import java.time.LocalDateTime;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.kodemi.config.LocalDateTimeConverter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "Blog")
public class Blog {

    @DynamoDBHashKey(attributeName = "blog_Id")
    private String blog_Id;

    @DynamoDBAttribute(attributeName = "title")
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

    // Values: DRAFT | PUBLISHED | REJECTED | HIDDEN
    @DynamoDBAttribute(attributeName = "status")
    private String status;

    @DynamoDBAttribute(attributeName = "thumbnail")
    private String thumbnail;

    @DynamoDBAttribute(attributeName = "trainer_id")
    private String trainer_id;

    // Set by admin when rejecting; cleared to null on re-submit
    @DynamoDBAttribute(attributeName = "rejection_reason")
    private String rejection_reason;

    // Audit: who reviewed and when
    @DynamoDBAttribute(attributeName = "reviewed_by")
    private String reviewed_by;

    @DynamoDBAttribute(attributeName = "reviewed_at")
    private String reviewed_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
