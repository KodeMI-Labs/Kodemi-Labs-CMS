package com.kodemi.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.SEO;

@Repository
public class SEORepository {
	private final DynamoDBMapper dynamoDBMapper;

	public SEORepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public SEO save(SEO seo) {
		dynamoDBMapper.save(seo);
		return seo;
	}

	public SEO findById(String seo_id) {
		return dynamoDBMapper.load(SEO.class, seo_id);
	}

	public List<SEO> findAll() {
		return dynamoDBMapper.scan(SEO.class, new DynamoDBScanExpression());
	}

	public void delete(String seo_id) {
		SEO seo = dynamoDBMapper.load(SEO.class, seo_id);
		if (seo != null) {
			dynamoDBMapper.delete(seo);
		}
	}
}
