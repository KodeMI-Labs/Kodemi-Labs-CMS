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
@DynamoDBTable(tableName = "SEO")
public class SEO {
	@DynamoDBHashKey(attributeName = "seo_id")
	private String seo_id;
	@DynamoDBAttribute(attributeName = "meta_title")
	private String meta_title;
	@DynamoDBAttribute(attributeName = "meta_description")
	private String meta_description;
	@DynamoDBAttribute(attributeName = "keywords")
	private List<String> keywords;
	@DynamoDBAttribute(attributeName = "canonical_url")
	private String canonical_url;
	@DynamoDBAttribute(attributeName = "robots_tag")
	private String robots_tag;
	@DynamoDBAttribute(attributeName = "og_title")
	private String og_title;
	@DynamoDBAttribute(attributeName = "og_description")
	private String og_description;
	@DynamoDBAttribute(attributeName = "og_image")
	private String og_image;
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "created_at")
	private LocalDateTime created_at;
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "updated_at")
	private LocalDateTime updated_at;
}
