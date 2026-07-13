package com.ContentManagementSystem.CMS.repository.reviewsandratingrepository;

import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorRatingSummary;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InstructorRatingSummaryRepository {
    public final DynamoDBMapper dynamoDBMapper;
    public InstructorRatingSummaryRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public InstructorRatingSummary save(InstructorRatingSummary instructorRatingSummary){
         dynamoDBMapper.save(instructorRatingSummary);
         return instructorRatingSummary;
    }
    public InstructorRatingSummary findById(String instructor_id){
        return dynamoDBMapper.load(InstructorRatingSummary.class,instructor_id);
    }
    public List<InstructorRatingSummary> findAll(){
        return dynamoDBMapper.scan(InstructorRatingSummary.class,new DynamoDBScanExpression());
    }
    public void delete(String instructor_id){
        InstructorRatingSummary instructorRatingSummary=dynamoDBMapper.load(InstructorRatingSummary.class,instructor_id);
        if(instructorRatingSummary!=null){
            dynamoDBMapper.delete(instructorRatingSummary);
        }
    }
}
