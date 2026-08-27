package com.kodemi.repository.preference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kodemi.model.PreferenceRule;

@Repository
public class PreferenceRuleRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public PreferenceRuleRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public PreferenceRule save(PreferenceRule rule) {
        dynamoDBMapper.save(rule);
        return rule;
    }

    public PreferenceRule findById(String rule_id) {
        return dynamoDBMapper.load(PreferenceRule.class, rule_id);
    }

    public List<PreferenceRule> findAll() {
        return dynamoDBMapper.scan(PreferenceRule.class, new DynamoDBScanExpression());
    }

    /** Returns all rules originating from a given source page. */
    public List<PreferenceRule> findBySourcePageId(String source_page_id) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":sourcePageId", new AttributeValue().withS(source_page_id));

        DynamoDBScanExpression scan = new DynamoDBScanExpression()
                .withFilterExpression("source_page_id = :sourcePageId")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(PreferenceRule.class, scan);
    }

    /**
     * Returns the active rule triggered by a specific option on a specific page.
     * Used at runtime to resolve next-page navigation.
     */
    public List<PreferenceRule> findBySourceOptionId(String source_option_id) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":sourceOptionId", new AttributeValue().withS(source_option_id));
        eav.put(":trueVal",        new AttributeValue().withBOOL(true));

        DynamoDBScanExpression scan = new DynamoDBScanExpression()
                .withFilterExpression("source_option_id = :sourceOptionId AND active = :trueVal")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(PreferenceRule.class, scan);
    }

    /** Returns all active rules pointing to a target page (useful for impact analysis). */
    public List<PreferenceRule> findByTargetPageId(String target_page_id) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":targetPageId", new AttributeValue().withS(target_page_id));

        DynamoDBScanExpression scan = new DynamoDBScanExpression()
                .withFilterExpression("target_page_id = :targetPageId")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(PreferenceRule.class, scan);
    }

    public void deleteById(String rule_id) {
        PreferenceRule rule = dynamoDBMapper.load(PreferenceRule.class, rule_id);
        if (rule != null) {
            dynamoDBMapper.delete(rule);
        }
    }
}
