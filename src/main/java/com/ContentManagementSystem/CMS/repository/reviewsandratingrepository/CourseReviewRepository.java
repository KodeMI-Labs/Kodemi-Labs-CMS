package com.ContentManagementSystem.CMS.repository.reviewsandratingrepository;

import com.ContentManagementSystem.CMS.model.reviewsandratings.CourseReview;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseReviewRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public CourseReviewRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public CourseReview save(CourseReview courseReview) {
        dynamoDBMapper.save(courseReview);
        return courseReview;
    }

    public CourseReview findById(String review_id) {
        return dynamoDBMapper.load(CourseReview.class, review_id);
    }

    public List<CourseReview> findAll() {
        return dynamoDBMapper.scan(CourseReview.class, new DynamoDBScanExpression());
    }

    /**
     * Query by course_id using the GSI "course-id-index".
     * Much more efficient than full scan — only returns reviews for the given course.
     */
    public List<CourseReview> findByCourseId(String courseId) {
        CourseReview keyObj = new CourseReview();
        keyObj.setCourse_id(courseId);

        DynamoDBQueryExpression<CourseReview> queryExpression = new DynamoDBQueryExpression<CourseReview>()
                .withIndexName("course-id-index")
                .withConsistentRead(false)   // GSI only supports eventual consistency
                .withHashKeyValues(keyObj);

        return dynamoDBMapper.query(CourseReview.class, queryExpression);
    }

    public void delete(String review_id) {
        CourseReview courseReview = dynamoDBMapper.load(CourseReview.class, review_id);
        if (courseReview != null) {
            dynamoDBMapper.delete(courseReview);
        }
    }
}
