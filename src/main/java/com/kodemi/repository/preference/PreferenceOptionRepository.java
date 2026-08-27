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
import com.kodemi.model.PreferenceOption;

@Repository
public class PreferenceOptionRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public PreferenceOptionRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public PreferenceOption save(PreferenceOption option) {
		dynamoDBMapper.save(option);
		return option;
	}

	public PreferenceOption findById(String option_id) {
		return dynamoDBMapper.load(PreferenceOption.class, option_id);
	}

	public List<PreferenceOption> findAll() {
		return dynamoDBMapper.scan(PreferenceOption.class, new DynamoDBScanExpression());
	}

	/** Returns all options for a page, sorted by display_order ascending. */
	public List<PreferenceOption> findByPageId(String page_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":pageId", new AttributeValue().withS(page_id));

		DynamoDBScanExpression scan = new DynamoDBScanExpression().withFilterExpression("page_id = :pageId")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferenceOption.class, scan).stream()
				.sorted(Comparator.comparingInt(o -> o.getDisplay_order() == null ? 0 : o.getDisplay_order()))
				.collect(Collectors.toList());
	}

	/**
	 * Returns only active options for a page, sorted by display_order ascending.
	 */
	public List<PreferenceOption> findActiveByPageId(String page_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":pageId", new AttributeValue().withS(page_id));
		eav.put(":trueVal", new AttributeValue().withBOOL(true));

		DynamoDBScanExpression scan = new DynamoDBScanExpression()
				.withFilterExpression("page_id = :pageId AND active = :trueVal").withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferenceOption.class, scan).stream()
				.sorted(Comparator.comparingInt(o -> o.getDisplay_order() == null ? 0 : o.getDisplay_order()))
				.collect(Collectors.toList());
	}

	/**
	 * Returns child options of a given parent option (Phase 3 – conditional
	 * options). e.g. parent = "option-design" → returns UI UX, Motion Graphics,
	 * Game Design …
	 */
	public List<PreferenceOption> findByParentOptionId(String parent_option_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":parentId", new AttributeValue().withS(parent_option_id));

		DynamoDBScanExpression scan = new DynamoDBScanExpression().withFilterExpression("parent_option_id = :parentId")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferenceOption.class, scan).stream()
				.sorted(Comparator.comparingInt(o -> o.getDisplay_order() == null ? 0 : o.getDisplay_order()))
				.collect(Collectors.toList());
	}

	public void deleteById(String option_id) {
		PreferenceOption option = dynamoDBMapper.load(PreferenceOption.class, option_id);
		if (option != null) {
			dynamoDBMapper.delete(option);
		}
	}
}
