package com.ContentManagementSystem.CMS.repository;

import com.ContentManagementSystem.CMS.model.SEO;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SEORepository {
    private final DynamoDBMapper dynamoDBMapper;
    public SEORepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public SEO save(SEO seo){
        dynamoDBMapper.save(seo);
        return seo;
    }
    public SEO findById(String seo_id){
        return dynamoDBMapper.load(SEO.class,seo_id);
    }
    public List<SEO> findAll(){
        return dynamoDBMapper.scan(SEO.class,new DynamoDBScanExpression());
    }
    public void delete(String seo_id){
        SEO seo=dynamoDBMapper.load(SEO.class,seo_id);
        if(seo!=null){
            dynamoDBMapper.delete(seo);
        }
    }
}
