package com.kodemi.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kodemi.model.Blog;

@Repository
public class BlogRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public BlogRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public Blog save(Blog blog) {
		dynamoDBMapper.save(blog);
		return blog;
	}

	public Blog findById(String blog_Id) {
		return dynamoDBMapper.load(Blog.class, blog_Id);
	}

	public List<Blog> findAll() {
		return dynamoDBMapper.scan(Blog.class, new DynamoDBScanExpression());
	}

	/** Returns all blogs with the given status (e.g. DRAFT, PUBLISHED, REJECTED) */
	public List<Blog> findByStatus(String status) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":statusVal", new AttributeValue().withS(status));

		DynamoDBScanExpression scan = new DynamoDBScanExpression().withFilterExpression("#st = :statusVal")
				.withExpressionAttributeNames(Map.of("#st", "status")).withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(Blog.class, scan);
	}

	/** Returns all blogs belonging to a specific trainer */
	public List<Blog> findByTrainerId(String trainerId) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":trainerId", new AttributeValue().withS(trainerId));

		DynamoDBScanExpression scan = new DynamoDBScanExpression().withFilterExpression("trainer_id = :trainerId")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(Blog.class, scan);
	}

	public void deleteById(String blog_id) {
		Blog blog = dynamoDBMapper.load(Blog.class, blog_id);
		if (blog != null) {
			dynamoDBMapper.delete(blog);
		}
	}
}
