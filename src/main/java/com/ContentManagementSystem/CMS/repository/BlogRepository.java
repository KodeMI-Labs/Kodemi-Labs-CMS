package com.ContentManagementSystem.CMS.repository;

import com.ContentManagementSystem.CMS.model.Blog;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogRepository {
    private final DynamoDBMapper dynamoDBMapper;
    public BlogRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper=dynamoDBMapper;
    }
    public Blog save(Blog blog){
        dynamoDBMapper.save(blog);
        return blog;
    }
    public Blog findById(String blog_Id){
        return dynamoDBMapper.load(Blog.class,blog_Id);
    }
    public List<Blog> findAll(){
        return dynamoDBMapper.scan(Blog.class,new DynamoDBScanExpression());
    }
    public void deleteById(String blog_id){
        Blog blog=dynamoDBMapper.load(Blog.class,blog_id);
        if(blog!=null){
            dynamoDBMapper.delete(blog);
        }
    }

}
