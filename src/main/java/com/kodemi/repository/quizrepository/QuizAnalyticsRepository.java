package com.kodemi.repository.quizrepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.quizmodel.QuizAnalytics;

@Repository
public class QuizAnalyticsRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public QuizAnalyticsRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public QuizAnalytics save(QuizAnalytics quizAnalytics) {
		dynamoDBMapper.save(quizAnalytics);
		return quizAnalytics;
	}

	public QuizAnalytics findById(String analytics_id) {
		return dynamoDBMapper.load(QuizAnalytics.class, analytics_id);
	}

	public List<QuizAnalytics> findAll() {
		return dynamoDBMapper.scan(QuizAnalytics.class, new DynamoDBScanExpression().withConsistentRead(true));
	}

	public void delete(String analytics_id) {
		QuizAnalytics analytics = dynamoDBMapper.load(QuizAnalytics.class, analytics_id);
		if (analytics != null) {
			dynamoDBMapper.delete(analytics);
		}
	}
}
