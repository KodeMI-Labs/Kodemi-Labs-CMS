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
@DynamoDBTable(tableName = "Banner")
public class Banner {
	@DynamoDBHashKey(attributeName = "banner_Id")
	private String banner_Id;
	@DynamoDBAttribute(attributeName = "title")
	private String title;
	@DynamoDBAttribute(attributeName = "subtitle")
	private String subtitle;
	@DynamoDBAttribute(attributeName = "image_url")
	private String image_url;
	@DynamoDBAttribute(attributeName = "redirect_url")
	private String redirect_url;
	@DynamoDBAttribute(attributeName = "priority")
	private Integer priority;
	@DynamoDBAttribute(attributeName = "status")
	private String status;
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "created_at")
	private LocalDateTime created_at;
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "updated_at")
	private LocalDateTime updated_at;
}
