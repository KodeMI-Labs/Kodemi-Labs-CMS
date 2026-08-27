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
import com.kodemi.model.PreferencePage;

@Repository
public class PreferencePageRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public PreferencePageRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public PreferencePage save(PreferencePage page) {
		dynamoDBMapper.save(page);
		return page;
	}

	public PreferencePage findById(String page_id) {
		return dynamoDBMapper.load(PreferencePage.class, page_id);
	}

	public List<PreferencePage> findAll() {
		return dynamoDBMapper.scan(PreferencePage.class, new DynamoDBScanExpression());
	}

	/** Returns all pages belonging to a journey, sorted by page_order ascending. */
	public List<PreferencePage> findByJourneyId(String journey_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":journeyId", new AttributeValue().withS(journey_id));

		DynamoDBScanExpression scan = new DynamoDBScanExpression().withFilterExpression("journey_id = :journeyId")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferencePage.class, scan).stream()
				.sorted(Comparator.comparingInt(p -> p.getPage_order() == null ? 0 : p.getPage_order()))
				.collect(Collectors.toList());
	}

	/** Returns only active pages for a journey, sorted by page_order ascending. */
	public List<PreferencePage> findActiveByJourneyId(String journey_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":journeyId", new AttributeValue().withS(journey_id));
		eav.put(":trueVal", new AttributeValue().withBOOL(true));

		DynamoDBScanExpression scan = new DynamoDBScanExpression()
				.withFilterExpression("journey_id = :journeyId AND active = :trueVal")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferencePage.class, scan).stream()
				.sorted(Comparator.comparingInt(p -> p.getPage_order() == null ? 0 : p.getPage_order()))
				.collect(Collectors.toList());
	}

	public void deleteById(String page_id) {
		PreferencePage page = dynamoDBMapper.load(PreferencePage.class, page_id);
		if (page != null) {
			dynamoDBMapper.delete(page);
		}
	}
}
