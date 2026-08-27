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
@DynamoDBTable(tableName = "PreferenceOption")
public class PreferenceOption {

    @DynamoDBHashKey(attributeName = "option_id")
    private String option_id;

    @DynamoDBAttribute(attributeName = "page_id")
    private String page_id;

    @DynamoDBAttribute(attributeName = "display_name")
    private String display_name;

    @DynamoDBAttribute(attributeName = "value")
    private String value;

    @DynamoDBAttribute(attributeName = "icon_url")
    private String icon_url;

    @DynamoDBAttribute(attributeName = "image_url")
    private String image_url;

    @DynamoDBAttribute(attributeName = "display_order")
    private Integer display_order;

    @DynamoDBAttribute(attributeName = "active")
    private Boolean active;

    @DynamoDBAttribute(attributeName = "parent_option_id")
    private String parent_option_id;

    @DynamoDBAttribute(attributeName = "next_page_id")
    private String next_page_id;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
