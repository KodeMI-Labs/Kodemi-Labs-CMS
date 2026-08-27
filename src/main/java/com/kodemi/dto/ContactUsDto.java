package com.kodemi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactUsDto {

	private String contact_id;
	private String name;
	private String email;
	private String contact_number;
	private String message;
	private String status;
	private String created_at;
	private String updated_at;
}
