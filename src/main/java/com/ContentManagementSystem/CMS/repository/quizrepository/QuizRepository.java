package com.ContentManagementSystem.CMS.repository.quizrepository;

import com.ContentManagementSystem.CMS.model.quizmodel.Quiz;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuizRepository {
    private final DynamoDBMapper dynamoDBMapper;
    public QuizRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public Quiz save(Quiz quiz){
        dynamoDBMapper.save(quiz);
        return quiz;
    }
    public Quiz findById(String quiz_id){
        return dynamoDBMapper.load(Quiz.class,quiz_id);
    }
    public List<Quiz> findAll(){
        return dynamoDBMapper.scan(Quiz.class,new DynamoDBScanExpression());
    }
    public void delete(String quiz_id){
        Quiz quiz=dynamoDBMapper.load(Quiz.class,quiz_id);
        if(quiz!=null){
            dynamoDBMapper.delete(quiz);
        }
    }
}
