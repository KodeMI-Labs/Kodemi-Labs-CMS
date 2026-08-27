package com.kodemi.repository.quizrepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.quizmodel.QuizQuestion;

@Repository
public class QuizQuestionRepository {
	public final DynamoDBMapper dynamoDBMapper;

	public QuizQuestionRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public QuizQuestion save(QuizQuestion quizQuestion) {
		dynamoDBMapper.save(quizQuestion);
		return quizQuestion;
	}

	public QuizQuestion findById(String questionId) {
		return dynamoDBMapper.load(QuizQuestion.class, questionId);
	}

	public List<QuizQuestion> findAll() {
		return dynamoDBMapper.scan(QuizQuestion.class, new DynamoDBScanExpression());
	}

	public void delete(String questionId) {
		QuizQuestion quizQuestion = dynamoDBMapper.load(QuizQuestion.class, questionId);
		if (quizQuestion != null) {
			dynamoDBMapper.delete(quizQuestion);
		}
	}
}
