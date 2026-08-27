package com.kodemi.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.ContactUs;

@Repository
public class ContactUsRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public ContactUsRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public ContactUs save(ContactUs contactUs) {
		dynamoDBMapper.save(contactUs);
		return contactUs;
	}

	public ContactUs findById(String contact_id) {
		return dynamoDBMapper.load(ContactUs.class, contact_id);
	}

	public List<ContactUs> findAll() {
		return dynamoDBMapper.scan(ContactUs.class, new DynamoDBScanExpression().withConsistentRead(true));
	}

	public void delete(String contact_id) {
		ContactUs contactUs = dynamoDBMapper.load(ContactUs.class, contact_id);
		if (contactUs != null) {
			dynamoDBMapper.delete(contactUs);
		}
	}
}
