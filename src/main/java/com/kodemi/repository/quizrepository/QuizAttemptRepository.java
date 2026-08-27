package com.kodemi.repository.quizrepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.quizmodel.QuizAttempt;

@Repository
public class QuizAttemptRepository {
    public final DynamoDBMapper dynamoDBMapper;
    public QuizAttemptRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public QuizAttempt save(QuizAttempt quizAttempt){
        dynamoDBMapper.save(quizAttempt);
        return quizAttempt;
    }
    public QuizAttempt findById(String attempt_id){
        return dynamoDBMapper.load(QuizAttempt.class,attempt_id);
    }
    public List<QuizAttempt> findAll(){
        return dynamoDBMapper.scan(QuizAttempt.class,
                new DynamoDBScanExpression().withConsistentRead(true));
    }
    public void delete(String attempt_id){
        QuizAttempt quizAttempt=dynamoDBMapper.load(QuizAttempt.class,attempt_id);
        if(quizAttempt!=null){
            dynamoDBMapper.delete(quizAttempt);
        }
    }
}
