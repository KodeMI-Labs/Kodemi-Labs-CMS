package com.kodemi.repository.preference;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kodemi.model.PreferenceRecommendation;

@Repository
public class PreferenceRecommendationRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public PreferenceRecommendationRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public PreferenceRecommendation save(PreferenceRecommendation recommendation) {
		dynamoDBMapper.save(recommendation);
		return recommendation;
	}

	public PreferenceRecommendation findById(String recommendation_id) {
		return dynamoDBMapper.load(PreferenceRecommendation.class, recommendation_id);
	}

	public List<PreferenceRecommendation> findAll() {
		return dynamoDBMapper.scan(PreferenceRecommendation.class, new DynamoDBScanExpression());
	}

	/**
	 * Returns all active recommendations for a given option, sorted by priority
	 * ascending (lower = higher priority). Used by the Learner service to suggest
	 * courses after onboarding.
	 */
	public List<PreferenceRecommendation> findActiveByOptionId(String option_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":optionId", new AttributeValue().withS(option_id));
		eav.put(":trueVal", new AttributeValue().withBOOL(true));

		DynamoDBScanExpression scan = new DynamoDBScanExpression()
				.withFilterExpression("option_id = :optionId AND active = :trueVal").withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferenceRecommendation.class, scan).stream()
				.sorted(Comparator
						.comparingInt((PreferenceRecommendation r) -> r.getPriority() == null ? 0 : r.getPriority()))
				.collect(Collectors.toList());
	}

	/**
	 * Returns all recommendations mapped to a course category (useful for
	 * analytics).
	 */
	public List<PreferenceRecommendation> findByCourseCategoryId(String course_category_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":categoryId", new AttributeValue().withS(course_category_id));

		DynamoDBScanExpression scan = new DynamoDBScanExpression()
				.withFilterExpression("course_category_id = :categoryId").withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferenceRecommendation.class, scan);
	}

	public void deleteById(String recommendation_id) {
		PreferenceRecommendation rec = dynamoDBMapper.load(PreferenceRecommendation.class, recommendation_id);
		if (rec != null) {
			dynamoDBMapper.delete(rec);
		}
	}
}
