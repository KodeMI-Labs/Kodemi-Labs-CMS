package com.ContentManagementSystem.CMS.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "Testimonial")
public class Testimonial {

    @DynamoDBHashKey(attributeName = "testimonial_id")
    private String testimonial_id;

    @DynamoDBAttribute(attributeName = "user_id")
    private String user_id;

    @DynamoDBAttribute(attributeName = "user_name")
    private String user_name;

    @DynamoDBAttribute(attributeName = "user_profile_image")
    private String user_profile_image;

    @DynamoDBAttribute(attributeName = "user_designation")
    private String user_designation;

    @DynamoDBAttribute(attributeName = "user_company")
    private String user_company;

    @DynamoDBAttribute(attributeName = "content")
    private String content;

    @DynamoDBAttribute(attributeName = "rating")
    private Integer rating;

    @DynamoDBAttribute(attributeName = "status")
    private String status;  // ACTIVE, INACTIVE, PENDING

    @DynamoDBAttribute(attributeName = "featured")
    private Boolean featured;

    @DynamoDBAttribute(attributeName = "created_at")
    private String created_at;

    @DynamoDBAttribute(attributeName = "updated_at")
    private String updated_at;
}
