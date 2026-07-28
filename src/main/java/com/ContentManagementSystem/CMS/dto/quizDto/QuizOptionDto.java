package com.ContentManagementSystem.CMS.dto.quizDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizOptionDto {
    private String optionId;
    private String questionId;
    private String optionText;
    private Boolean correct;
}
