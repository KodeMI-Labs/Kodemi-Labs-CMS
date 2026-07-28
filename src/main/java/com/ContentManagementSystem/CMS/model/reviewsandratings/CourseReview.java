package com.ContentManagementSystem.CMS.model.reviewsandratings;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "CourseReview")
public class CourseReview {

    @DynamoDBHashKey(attributeName = "review_id")
    private String review_id;

    /**
     * Foreign reference to Course.courseId from Course Microservice.
     * Uses GSI "course-id-index" for efficient queries by course.
     */
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "course-id-index", attributeName = "course_id")
    private String course_id;

    /**
     * Foreign reference to User.learnerId from Auth Microservice.
     */
    @DynamoDBAttribute(attributeName = "learner_id")
    private String learner_id;

    @DynamoDBAttribute(attributeName = "learner_name")
    private String learner_name;

    @DynamoDBAttribute(attributeName = "learner_profile_image")
    private String learner_profile_image;

    @DynamoDBAttribute(attributeName = "rating")
    private Integer rating;

    @DynamoDBAttribute(attributeName = "review_comment")
    private String review_comment;

    @DynamoDBAttribute(attributeName = "review_at")
    private String review_at;

    @DynamoDBAttribute(attributeName = "edited")
    private Boolean edited;

    @DynamoDBAttribute(attributeName = "approved")
    private Boolean approved;
}
