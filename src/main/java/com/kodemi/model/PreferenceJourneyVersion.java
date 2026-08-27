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
@DynamoDBTable(tableName = "PreferenceJourneyVersion")
public class PreferenceJourneyVersion {

    @DynamoDBHashKey(attributeName = "version_id")
    private String version_id;

    @DynamoDBAttribute(attributeName = "journey_id")
    private String journey_id;

    @DynamoDBAttribute(attributeName = "version")
    private Integer version;

    @DynamoDBAttribute(attributeName = "active")
    private Boolean active;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "published_at")
    private LocalDateTime published_at;

    @DynamoDBAttribute(attributeName = "remarks")
    private String remarks;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;
}
