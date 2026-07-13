package com.ContentManagementSystem.CMS.repository;

import com.ContentManagementSystem.CMS.model.Banner;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BannerRepository {
    private final DynamoDBMapper dynamoDBMapper;
    public BannerRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public Banner findById(String banner_Id){
        return dynamoDBMapper.load(Banner.class,banner_Id);
    }
    public List<Banner> findAll(){
        return dynamoDBMapper.scan(Banner.class,new DynamoDBScanExpression());
    }
    public void delete(String banner_Id){
        Banner banner=dynamoDBMapper.load(Banner.class,banner_Id);
        if(banner!=null){
            dynamoDBMapper.delete(banner);
        }
    }
    public Banner save(Banner banner) {
        dynamoDBMapper.save(banner);
        return banner;
    }
}
