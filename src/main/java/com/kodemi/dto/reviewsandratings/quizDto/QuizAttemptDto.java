package com.kodemi.dto.reviewsandratings.quizDto;

import java.time.LocalDateTime;

import com.kodemi.enums.AttemptStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizAttemptDto {
	private String attempt_id;
	private String quiz_id;
	private String learnerId;
	private String learner_name;
	private Integer score;
	private Integer correctAnswers;
	private Integer wrongAnswers;
	private Double accuracy;
	private AttemptStatus status;
	private LocalDateTime startedAt;
	private LocalDateTime submittedAt;
	private Integer timeSpentMinutes;
}
