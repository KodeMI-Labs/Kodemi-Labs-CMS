package com.kodemi.model.quizmodel;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.kodemi.config.LocalDateTimeConverter;
import com.kodemi.enums.AttemptStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "QuizAttempt")
public class QuizAttempt {
	@DynamoDBHashKey(attributeName = "attempt_id")
	private String attempt_id;
	@DynamoDBAttribute(attributeName = "quiz_id")
	private String quiz_id;
	@DynamoDBAttribute(attributeName = "learnerId")
	private String learnerId;
	@DynamoDBAttribute(attributeName = "learner_name")
	private String learner_name;
	@DynamoDBAttribute(attributeName = "score")
	private Integer score;
	@DynamoDBAttribute(attributeName = "correctAnswers")
	private Integer correctAnswers;
	@DynamoDBAttribute(attributeName = "wrongAnswers")
	private Integer wrongAnswers;
	@DynamoDBAttribute(attributeName = "accuracy")
	private Double accuracy;
	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "status")
	private AttemptStatus status;
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "startedAt")
	private LocalDateTime startedAt;
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "submittedAt")
	private LocalDateTime submittedAt;
	@DynamoDBAttribute(attributeName = "timeSpentMinutes")
	private Integer timeSpentMinutes;
}