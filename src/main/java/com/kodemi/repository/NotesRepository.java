package com.kodemi.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kodemi.model.Notes;

@Repository
public class NotesRepository {
	private final DynamoDBMapper dynamoDBMapper;

	public NotesRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	public Notes save(Notes notes) {
		dynamoDBMapper.save(notes);
		return notes;
	}

	public Notes findById(String note_Id) {
		return dynamoDBMapper.load(Notes.class, note_Id);
	}

	public List<Notes> findAll() {
		return dynamoDBMapper.scan(Notes.class, new DynamoDBScanExpression());
	}

	public void delete(String note_Id) {
		Notes notes = dynamoDBMapper.load(Notes.class, note_Id);
		if (notes != null) {
			dynamoDBMapper.delete(notes);
		}
	}
}
