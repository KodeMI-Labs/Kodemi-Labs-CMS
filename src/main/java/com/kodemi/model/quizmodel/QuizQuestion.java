package com.kodemi.model.quizmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.kodemi.enums.DifficultyLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "QuizQuestion")
public class QuizQuestion {

	@DynamoDBHashKey(attributeName = "question_id")
	private String questionId;
	@DynamoDBAttribute(attributeName = "quiz_id")
	private String quiz_id;
	@DynamoDBAttribute(attributeName = "question")
	private String question;
	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "difficultyLevel")
	private DifficultyLevel difficultyLevel;
	@DynamoDBAttribute(attributeName = "marks")
	private Integer marks;
	@DynamoDBAttribute(attributeName = "sequenceNo")
	private Integer sequenceNo;
}