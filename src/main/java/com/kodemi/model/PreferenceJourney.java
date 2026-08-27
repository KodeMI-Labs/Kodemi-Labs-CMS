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
@DynamoDBTable(tableName = "PreferenceJourney")
public class PreferenceJourney {

	@DynamoDBHashKey(attributeName = "journey_id")
	private String journey_id;

	@DynamoDBAttribute(attributeName = "journey_name")
	private String journey_name;

	@DynamoDBAttribute(attributeName = "description")
	private String description;

	@DynamoDBAttribute(attributeName = "version")
	private Integer version;

	@DynamoDBAttribute(attributeName = "status")
	private String status;

	@DynamoDBAttribute(attributeName = "active")
	private Boolean active;

	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "publish_date")
	private LocalDateTime publish_date;

	@DynamoDBAttribute(attributeName = "created_by")
	private String created_by;

	@DynamoDBAttribute(attributeName = "updated_by")
	private String updated_by;

	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "created_at")
	private LocalDateTime created_at;

	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "updated_at")
	private LocalDateTime updated_at;
}
