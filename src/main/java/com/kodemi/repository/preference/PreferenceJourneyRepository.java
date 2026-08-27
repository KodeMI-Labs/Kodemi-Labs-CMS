package com.kodemi.repository.preference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kodemi.model.PreferenceJourney;

@Repository
public class PreferenceJourneyRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public PreferenceJourneyRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public PreferenceJourney save(PreferenceJourney journey) {
		dynamoDBMapper.save(journey);
		return journey;
	}

	public PreferenceJourney findById(String journey_id) {
		return dynamoDBMapper.load(PreferenceJourney.class, journey_id);
	}

	public List<PreferenceJourney> findAll() {
		return dynamoDBMapper.scan(PreferenceJourney.class, new DynamoDBScanExpression());
	}

	/**
	 * Returns all journeys matching the given status (DRAFT / PUBLISHED /
	 * ARCHIVED).
	 */
	public List<PreferenceJourney> findByStatus(String status) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":statusVal", new AttributeValue().withS(status));

		DynamoDBScanExpression scan = new DynamoDBScanExpression().withFilterExpression("#st = :statusVal")
				.withExpressionAttributeNames(Map.of("#st", "status")).withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferenceJourney.class, scan);
	}

	/**
	 * Returns the single currently active (PUBLISHED) journey. There should always
	 * be at most one; the service enforces this on publish.
	 */
	public List<PreferenceJourney> findActiveJourneys() {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":trueVal", new AttributeValue().withBOOL(true));
		eav.put(":publishedVal", new AttributeValue().withS("PUBLISHED"));

		DynamoDBScanExpression scan = new DynamoDBScanExpression()
				.withFilterExpression("active = :trueVal AND #st = :publishedVal")
				.withExpressionAttributeNames(Map.of("#st", "status")).withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferenceJourney.class, scan);
	}

	public void deleteById(String journey_id) {
		PreferenceJourney journey = dynamoDBMapper.load(PreferenceJourney.class, journey_id);
		if (journey != null) {
			dynamoDBMapper.delete(journey);
		}
	}
}
