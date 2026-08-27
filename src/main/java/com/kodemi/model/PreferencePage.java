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
@DynamoDBTable(tableName = "PreferencePage")
public class PreferencePage {

    @DynamoDBHashKey(attributeName = "page_id")
    private String page_id;

    @DynamoDBAttribute(attributeName = "journey_id")
    private String journey_id;

    @DynamoDBAttribute(attributeName = "page_order")
    private Integer page_order;

    @DynamoDBAttribute(attributeName = "title")
    private String title;

    @DynamoDBAttribute(attributeName = "subtitle")
    private String subtitle;

    @DynamoDBAttribute(attributeName = "description")
    private String description;

    @DynamoDBAttribute(attributeName = "field_type")
    private String field_type;

    @DynamoDBAttribute(attributeName = "selection_type")
    private String selection_type;

    @DynamoDBAttribute(attributeName = "searchable")
    private Boolean searchable;

    @DynamoDBAttribute(attributeName = "mandatory")
    private Boolean mandatory;

    @DynamoDBAttribute(attributeName = "active")
    private Boolean active;

    @DynamoDBAttribute(attributeName = "next_page_id")
    private String next_page_id;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
