package com.kodemi.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.Testimonial;

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
