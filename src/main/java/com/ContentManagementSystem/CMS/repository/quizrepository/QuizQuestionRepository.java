package com.ContentManagementSystem.CMS.repository.quizrepository;

import com.ContentManagementSystem.CMS.model.quizmodel.QuizQuestion;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuizQuestionRepository {
    public final DynamoDBMapper dynamoDBMapper;
    public QuizQuestionRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public QuizQuestion save(QuizQuestion quizQuestion){
         dynamoDBMapper.save(quizQuestion);
         return quizQuestion;
    }
    public QuizQuestion findById(String questionId){
        return dynamoDBMapper.load(QuizQuestion.class,questionId);
    }
    public List<QuizQuestion> findAll(){
        return dynamoDBMapper.scan(QuizQuestion.class,new DynamoDBScanExpression());
    }
    public void delete(String questionId){
        QuizQuestion quizQuestion=dynamoDBMapper.load(QuizQuestion.class,questionId);
        if(quizQuestion!=null){
            dynamoDBMapper.
                    delete(quizQuestion);
        }
    }
}
