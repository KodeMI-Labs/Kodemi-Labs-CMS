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
@DynamoDBTable(tableName = "PreferenceRule")
public class PreferenceRule {

    @DynamoDBHashKey(attributeName = "rule_id")
    private String rule_id;

    @DynamoDBAttribute(attributeName = "source_page_id")
    private String source_page_id;

    @DynamoDBAttribute(attributeName = "source_option_id")
    private String source_option_id;

    @DynamoDBAttribute(attributeName = "target_page_id")
    private String target_page_id;

    @DynamoDBAttribute(attributeName = "rule_type")
    private String rule_type;

    @DynamoDBAttribute(attributeName = "active")
    private Boolean active;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
