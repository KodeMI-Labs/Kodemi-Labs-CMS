package com.kodemi.repository.quizrepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kodemi.model.quizmodel.Quiz;

@Repository
public class QuizRepository {
	private final DynamoDBMapper dynamoDBMapper;

	public QuizRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public Quiz save(Quiz quiz) {
		dynamoDBMapper.save(quiz);
		return quiz;
	}

	public Quiz findById(String quiz_id) {
		return dynamoDBMapper.load(Quiz.class, quiz_id);
	}

	public List<Quiz> findAll() {
		return dynamoDBMapper.scan(Quiz.class, new DynamoDBScanExpression());
	}

	public void delete(String quiz_id) {
		Quiz quiz = dynamoDBMapper.load(Quiz.class, quiz_id);
		if (quiz != null) {
			dynamoDBMapper.delete(quiz);
		}
	}
	
	public List<Quiz> findByTrainerId(String trainer_id) {
	    Map<String, AttributeValue> eav = new HashMap<>();
	    eav.put(":val", new AttributeValue().withS(trainer_id));

	    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
	            .withFilterExpression("trainer_id = :val")
	            .withExpressionAttributeValues(eav);

	    return dynamoDBMapper.scan(Quiz.class, scanExpression);
	}
}
