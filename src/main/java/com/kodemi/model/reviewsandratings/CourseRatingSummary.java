package com.kodemi.model.reviewsandratings;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "CourseRatingSummary")
public class CourseRatingSummary {

    /**
     * Primary key AND foreign reference to Course.courseId.
     * One summary per course — course_id IS the hash key here.
     * Linked to CourseReview.course_id (1:many relationship in code).
     */
    @DynamoDBHashKey(attributeName = "course_id")
    private String course_id;

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
    private Integer two_star_count;

    @DynamoDBAttribute(attributeName = "one_star_count")
    private Integer one_star_count;
}