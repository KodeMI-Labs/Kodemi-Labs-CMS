package com.ContentManagementSystem.CMS.dto.quizDto;

import com.ContentManagementSystem.CMS.enums.AttemptStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
