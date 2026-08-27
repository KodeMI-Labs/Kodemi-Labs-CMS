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
import com.kodemi.model.PreferenceJourneyVersion;

@Repository
public class PreferenceJourneyVersionRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public PreferenceJourneyVersionRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public PreferenceJourneyVersion save(PreferenceJourneyVersion version) {
		dynamoDBMapper.save(version);
		return version;
	}

	public PreferenceJourneyVersion findById(String version_id) {
		return dynamoDBMapper.load(PreferenceJourneyVersion.class, version_id);
	}

	public List<PreferenceJourneyVersion> findAll() {
		return dynamoDBMapper.scan(PreferenceJourneyVersion.class, new DynamoDBScanExpression());
	}

	/**
	 * Returns all version records for a journey, sorted by version number
	 * descending so the latest version is always first.
	 */
	public List<PreferenceJourneyVersion> findByJourneyId(String journey_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":journeyId", new AttributeValue().withS(journey_id));

		DynamoDBScanExpression scan = new DynamoDBScanExpression().withFilterExpression("journey_id = :journeyId")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferenceJourneyVersion.class, scan).stream().sorted(Comparator
				.comparingInt((PreferenceJourneyVersion v) -> v.getVersion() == null ? 0 : v.getVersion()).reversed())
				.collect(Collectors.toList());
	}

	/** Returns the single currently active version for a journey. */
	public List<PreferenceJourneyVersion> findActiveByJourneyId(String journey_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":journeyId", new AttributeValue().withS(journey_id));
		eav.put(":trueVal", new AttributeValue().withBOOL(true));

		DynamoDBScanExpression scan = new DynamoDBScanExpression()
				.withFilterExpression("journey_id = :journeyId AND active = :trueVal")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(PreferenceJourneyVersion.class, scan);
	}

	public void deleteById(String version_id) {
		PreferenceJourneyVersion version = dynamoDBMapper.load(PreferenceJourneyVersion.class, version_id);
		if (version != null) {
			dynamoDBMapper.delete(version);
		}
	}
}
