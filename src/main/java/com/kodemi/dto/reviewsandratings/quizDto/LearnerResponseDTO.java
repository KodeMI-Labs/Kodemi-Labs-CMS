package com.kodemi.dto.reviewsandratings.quizDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LearnerResponseDTO {
	private String username;
	private String email;
	private String fullName;
}