package com.kodemi.repository.quizrepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.quizmodel.QuizCategory;

@Repository
public class QuizCategoryRepository {
    private final DynamoDBMapper dynamoDBMapper;
    public QuizCategoryRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public QuizCategory save(QuizCategory quizCategory){
        dynamoDBMapper.save(quizCategory);
        return quizCategory;
    }
    public QuizCategory findById(String categoryId){
        return dynamoDBMapper.load(QuizCategory.class,categoryId);
    }
    public List<QuizCategory> findAll(){
        return dynamoDBMapper.scan(QuizCategory.class,new DynamoDBScanExpression());
    }
    public void delete(String categoryId){
        QuizCategory quizCategory=dynamoDBMapper.load(QuizCategory.class,categoryId);
        if(quizCategory!=null){
            dynamoDBMapper.delete(quizCategory);
        }
    }
}
