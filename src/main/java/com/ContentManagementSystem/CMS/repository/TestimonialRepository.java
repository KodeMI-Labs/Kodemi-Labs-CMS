package com.ContentManagementSystem.CMS.repository;

import com.ContentManagementSystem.CMS.model.Testimonial;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TestimonialRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public TestimonialRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Testimonial save(Testimonial testimonial) {
        dynamoDBMapper.save(testimonial);
        return testimonial;
    }

    public Testimonial findById(String testimonial_id) {
        return dynamoDBMapper.load(Testimonial.class, testimonial_id);
    }

    public List<Testimonial> findAll() {
        return dynamoDBMapper.scan(Testimonial.class,
                new DynamoDBScanExpression().withConsistentRead(true));
    }

    public void delete(String testimonial_id) {
        Testimonial testimonial = dynamoDBMapper.load(Testimonial.class, testimonial_id);
        if (testimonial != null) {
            dynamoDBMapper.delete(testimonial);
        }
    }
}
