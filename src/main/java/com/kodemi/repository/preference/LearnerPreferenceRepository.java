package com.kodemi.repository.preference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kodemi.model.LearnerPreference;

@Repository
public class LearnerPreferenceRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public LearnerPreferenceRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	/** Upsert — saves or overwrites the learner's preference document. */
	public LearnerPreference save(LearnerPreference preference) {
		dynamoDBMapper.save(preference);
		return preference;
	}

	/** Direct hash-key load. Returns null if not found. */
	public LearnerPreference findByLearnerId(String learner_id) {
		return dynamoDBMapper.load(LearnerPreference.class, learner_id);
	}

	/** All preference records — used for admin analytics. */
	public List<LearnerPreference> findAll() {
		return dynamoDBMapper.scan(LearnerPreference.class, new DynamoDBScanExpression());
	}

	/** All learners who completed onboarding for a specific journey. */
	public List<LearnerPreference> findCompletedByJourneyId(String journey_id) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":journeyId", new AttributeValue().withS(journey_id));
		eav.put(":trueVal", new AttributeValue().withBOOL(true));

		DynamoDBScanExpression scan = new DynamoDBScanExpression()
				.withFilterExpression("journey_id = :journeyId AND completed = :trueVal")
				.withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(LearnerPreference.class, scan);
	}

	public void deleteByLearnerId(String learner_id) {
		LearnerPreference preference = dynamoDBMapper.load(LearnerPreference.class, learner_id);
		if (preference != null) {
			dynamoDBMapper.delete(preference);
		}
	}
}
