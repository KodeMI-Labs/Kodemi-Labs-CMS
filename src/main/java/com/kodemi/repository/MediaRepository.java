package com.kodemi.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.Media;

@Repository
public class MediaRepository {
	private final DynamoDBMapper dynamoDBMapper;

	public MediaRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public Media save(Media media) {
		dynamoDBMapper.save(media);
		return media;
	}

	public Media findById(String meta_Id) {
		return dynamoDBMapper.load(Media.class, meta_Id);
	}

	public List<Media> findAll() {
		return dynamoDBMapper.scan(Media.class, new DynamoDBScanExpression());
	}

	public void delete(String meta_id) {
		Media media = dynamoDBMapper.load(Media.class, meta_id);
		if (media != null) {
			dynamoDBMapper.delete(media);
		}
	}
}
