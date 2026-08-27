package com.kodemi.repository.quizrepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.quizmodel.QuizAnswer;

@Repository
public class QuizAnswerRepository {
	private final DynamoDBMapper dynamoDBMapper;

	public QuizAnswerRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public QuizAnswer save(QuizAnswer quizAnswer) {
		dynamoDBMapper.save(quizAnswer);
		return quizAnswer;
	}

	public QuizAnswer findById(String answer_id) {
		return dynamoDBMapper.load(QuizAnswer.class, answer_id);
	}

	public List<QuizAnswer> findAll() {
		return dynamoDBMapper.scan(QuizAnswer.class, new DynamoDBScanExpression());
	}

	public void delete(String answer_id) {
		QuizAnswer quizAnswer = dynamoDBMapper.load(QuizAnswer.class, answer_id);
		if (quizAnswer != null) {
			dynamoDBMapper.delete(quizAnswer);
		}
	}
}
