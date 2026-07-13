package com.ContentManagementSystem.CMS.repository.reviewsandratingrepository;

import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorReview;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InstructorReviewRepository {
    private final DynamoDBMapper dynamoDBMapper;
    public InstructorReviewRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public InstructorReview save(InstructorReview instructorReview){
        dynamoDBMapper.save(instructorReview);
        return instructorReview;
    }
    public InstructorReview findById(String review_id){
        return dynamoDBMapper.load(InstructorReview.class,review_id);
    }
    public List<InstructorReview> findAll(){
        return dynamoDBMapper.scan(InstructorReview.class,new DynamoDBScanExpression());
    }
    public void delete(String review_id){
        InstructorReview instructorReview=dynamoDBMapper.load(InstructorReview.class,review_id);
        if(instructorReview!=null){
            dynamoDBMapper.delete(instructorReview);
        }
    }
}
