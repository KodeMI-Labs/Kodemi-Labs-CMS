package com.kodemi.repository.preference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kodemi.model.LearnerJourneyProgress;

@Repository
public class LearnerJourneyProgressRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public LearnerJourneyProgressRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public LearnerJourneyProgress save(LearnerJourneyProgress progress) {
        dynamoDBMapper.save(progress);
        return progress;
    }

    public LearnerJourneyProgress findById(String progress_id) {
        return dynamoDBMapper.load(LearnerJourneyProgress.class, progress_id);
    }

    /** All progress records for a learner (one per journey they have started). */
    public List<LearnerJourneyProgress> findByLearnerId(String learner_id) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":learnerId", new AttributeValue().withS(learner_id));

        DynamoDBScanExpression scan = new DynamoDBScanExpression()
                .withFilterExpression("learner_id = :learnerId")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(LearnerJourneyProgress.class, scan);
    }

    /** Incomplete progress records — used to determine if onboarding can be resumed. */
    public List<LearnerJourneyProgress> findIncompleteByLearnerId(String learner_id) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":learnerId", new AttributeValue().withS(learner_id));
        eav.put(":falseVal",  new AttributeValue().withBOOL(false));

        DynamoDBScanExpression scan = new DynamoDBScanExpression()
                .withFilterExpression("learner_id = :learnerId AND completed = :falseVal")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(LearnerJourneyProgress.class, scan);
    }

    public void deleteById(String progress_id) {
        LearnerJourneyProgress progress = dynamoDBMapper.load(LearnerJourneyProgress.class, progress_id);
        if (progress != null) {
            dynamoDBMapper.delete(progress);
        }
    }
}
