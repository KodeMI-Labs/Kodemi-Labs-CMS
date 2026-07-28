package com.ContentManagementSystem.CMS.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.ContentManagementSystem.CMS.config.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@DynamoDBTable(tableName = "Media")
public class Media {
    @DynamoDBHashKey(attributeName = "meta_Id")
    private String meta_Id;
    @DynamoDBAttribute(attributeName = "file_name")
    private String file_name;
    @DynamoDBAttribute(attributeName = "file_type")
    private String file_type;
    @DynamoDBAttribute(attributeName = "file_url")
    private String file_url;
    @DynamoDBAttribute(attributeName = "uploaded_by")
    private String uploaded_by;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "created_at")
    private LocalDateTime created_at;
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "updated_at")
    private LocalDateTime updated_at;
}
