package com.ContentManagementSystem.CMS.repository.quizrepository;

import com.ContentManagementSystem.CMS.model.quizmodel.QuizOption;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuizOptionRepository {
    private final DynamoDBMapper dynamoDBMapper;
    public QuizOptionRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public QuizOption save(QuizOption quizOption){
        dynamoDBMapper.save(quizOption);
        return quizOption;
    }
    public QuizOption findById(String optionId){
        return dynamoDBMapper.load(QuizOption.class,optionId);
    }
    public List<QuizOption> findAll(){
        return dynamoDBMapper.scan(QuizOption.class,new DynamoDBScanExpression());
    }
    public void delete(String optionId){
        QuizOption quizOption=dynamoDBMapper.load(QuizOption.class,optionId);
        if(quizOption!=null){
            dynamoDBMapper.delete(quizOption);
        }
    }
}
