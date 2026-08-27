package com.kodemi.repository.reviewsandratingrepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.reviewsandratings.InstructorReview;

@Repository
public class InstructorReviewRepository {
	private final DynamoDBMapper dynamoDBMapper;

	public InstructorReviewRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public InstructorReview save(InstructorReview instructorReview) {
		dynamoDBMapper.save(instructorReview);
		return instructorReview;
	}

	public InstructorReview findById(String review_id) {
		return dynamoDBMapper.load(InstructorReview.class, review_id);
	}

	public List<InstructorReview> findAll() {
		return dynamoDBMapper.scan(InstructorReview.class, new DynamoDBScanExpression());
	}

	public void delete(String review_id) {
		InstructorReview instructorReview = dynamoDBMapper.load(InstructorReview.class, review_id);
		if (instructorReview != null) {
			dynamoDBMapper.delete(instructorReview);
		}
	}
}
