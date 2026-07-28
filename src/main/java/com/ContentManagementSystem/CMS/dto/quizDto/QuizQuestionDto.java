package com.ContentManagementSystem.CMS.dto.quizDto;

import com.ContentManagementSystem.CMS.enums.DifficultyLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizQuestionDto {
    private String questionId;
    private String quiz_id;
    private String question;
    private DifficultyLevel difficultyLevel;
    private Integer marks;
    private Integer sequenceNo;
}
