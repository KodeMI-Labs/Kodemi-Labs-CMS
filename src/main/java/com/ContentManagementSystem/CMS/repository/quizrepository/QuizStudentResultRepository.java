package com.ContentManagementSystem.CMS.repository.quizrepository;

import com.ContentManagementSystem.CMS.model.quizmodel.QuizStudentResult;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuizStudentResultRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public QuizStudentResultRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public QuizStudentResult save(QuizStudentResult result) {
        dynamoDBMapper.save(result);
        return result;
    }

    public QuizStudentResult findById(String result_id) {
        return dynamoDBMapper.load(QuizStudentResult.class, result_id);
    }

    public List<QuizStudentResult> findAll() {
        return dynamoDBMapper.scan(QuizStudentResult.class,
                new DynamoDBScanExpression().withConsistentRead(true));
    }

    public void delete(String result_id) {
        QuizStudentResult result = dynamoDBMapper.load(QuizStudentResult.class, result_id);
        if (result != null) {
            dynamoDBMapper.delete(result);
        }
    }
}
