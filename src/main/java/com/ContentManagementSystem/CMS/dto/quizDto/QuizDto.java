package com.ContentManagementSystem.CMS.dto.quizDto;

import com.ContentManagementSystem.CMS.enums.QuizStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuizDto {
    private String quiz_id;
    private String title;
    private String description;
    private String category;
    private String trainer_id;
    private Integer durationMinutes;
    private Integer totalQuestion;
    private QuizStatus status;
    private Double averageRating;
    private Integer totalAttempts;
    private Integer totalMarks;
    private LocalDateTime quizDate;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
