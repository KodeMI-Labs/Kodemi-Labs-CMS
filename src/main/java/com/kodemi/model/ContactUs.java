package com.kodemi.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "ContactUs")
public class ContactUs {
	@DynamoDBHashKey(attributeName = "contact_id")
	private String contact_id;
	@DynamoDBAttribute(attributeName = "name")
	private String name;
	@DynamoDBAttribute(attributeName = "email")
	private String email;
	@DynamoDBAttribute(attributeName = "contact_number")
	private String contact_number;
	@DynamoDBAttribute(attributeName = "message")
	private String message;
	// UNREAD | READ | RESOLVED
	@DynamoDBAttribute(attributeName = "status")
	private String status;
	@DynamoDBAttribute(attributeName = "created_at")
	private String created_at;
	@DynamoDBAttribute(attributeName = "updated_at")
	private String updated_at;
}
