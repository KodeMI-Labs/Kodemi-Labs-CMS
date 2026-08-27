package com.kodemi.repository.reviewsandratingrepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.reviewsandratings.CourseRatingSummary;

@Repository
public class CourseRatingSummaryRepository {
	private final DynamoDBMapper dynamoDBMapper;

	public CourseRatingSummaryRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public CourseRatingSummary save(CourseRatingSummary courseRatingSummary) {
		dynamoDBMapper.save(courseRatingSummary);
		return courseRatingSummary;
	}

	public CourseRatingSummary findById(String course_id) {
		return dynamoDBMapper.load(CourseRatingSummary.class, course_id);
	}

	public List<CourseRatingSummary> findAll() {
		return dynamoDBMapper.scan(CourseRatingSummary.class, new DynamoDBScanExpression());
	}

	public void delete(String course_id) {
		CourseRatingSummary courseRatingSummary = dynamoDBMapper.load(CourseRatingSummary.class, course_id);
		if (courseRatingSummary != null) {
			dynamoDBMapper.delete(courseRatingSummary);
		}
	}
}
