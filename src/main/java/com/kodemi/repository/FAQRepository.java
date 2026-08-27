package com.kodemi.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.FAQ;

@Repository
public class FAQRepository {
	private final DynamoDBMapper dynamoDBMapper;

	public FAQRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public FAQ save(FAQ faq) {
		dynamoDBMapper.save(faq);
		return faq;
	}

	public FAQ findById(String faq_id) {
		return dynamoDBMapper.load(FAQ.class, faq_id);
	}

	public List<FAQ> findAll() {
		return dynamoDBMapper.scan(FAQ.class, new DynamoDBScanExpression());
	}

	public void delete(String faq_id) {
		FAQ faq = dynamoDBMapper.load(FAQ.class, faq_id);
		if (faq != null) {
			dynamoDBMapper.delete(faq);
		}
	}
}
