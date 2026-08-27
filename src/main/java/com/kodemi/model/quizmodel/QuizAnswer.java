package com.kodemi.model.quizmodel;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.kodemi.config.LocalDateTimeConverter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "QuizAnswer")
public class QuizAnswer {

	@DynamoDBHashKey(attributeName = "answer_id")
	private String answer_id;
	@DynamoDBAttribute(attributeName = "attempt_id")
	private String attempt_id;
	@DynamoDBAttribute(attributeName = "question_id")
	private String question_id;
	@DynamoDBAttribute(attributeName = "selectedOption_id")
	private String selectedOption_id;
	@DynamoDBAttribute(attributeName = "correct")
	private Boolean correct;
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	@DynamoDBAttribute(attributeName = "answeredAt")
	private LocalDateTime answeredAt;
}