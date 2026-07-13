package com.ContentManagementSystem.CMS.repository;

import com.ContentManagementSystem.CMS.model.StaticPages;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StaticPagesRepository {
    private final DynamoDBMapper dynamoDBMapper;
    public StaticPagesRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public StaticPages save(StaticPages staticPages){
        dynamoDBMapper.save(staticPages);
        return staticPages;
    }
    public StaticPages findById(String page_id){
        return dynamoDBMapper.load(StaticPages.class,page_id);
    }
    public List<StaticPages> findAll(){
        return dynamoDBMapper.scan(StaticPages.class,new DynamoDBScanExpression());
    }
    public void delete(String page_id){
        StaticPages staticPages=dynamoDBMapper.load(StaticPages.class,page_id);
        if(staticPages!=null){
            dynamoDBMapper.delete(staticPages);
        }
    }
}
