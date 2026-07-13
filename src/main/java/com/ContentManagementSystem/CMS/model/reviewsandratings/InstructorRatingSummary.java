package com.ContentManagementSystem.CMS.model.reviewsandratings;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "InstructorRatingSummary")
public class InstructorRatingSummary {
    @DynamoDBHashKey(attributeName = "instructor_id")
    private String instructor_id;
    @DynamoDBAttribute(attributeName = "average_rating")
    private Double average_rating;
    @DynamoDBAttribute(attributeName = "total_reviews")
    private Integer total_reviews;
    @DynamoDBAttribute(attributeName = "five_star_count")
    private Integer five_star_count;
    @DynamoDBAttribute(attributeName = "four_star_count")
    private Integer four_star_count;
    @DynamoDBAttribute(attributeName = "three_star_count")
    private Integer three_star_count;
    @DynamoDBAttribute(attributeName = "two_star_count")
    private Integer twoStarCount;
    @DynamoDBAttribute(attributeName = "one_star_count")
    private Integer one_star_count;
}