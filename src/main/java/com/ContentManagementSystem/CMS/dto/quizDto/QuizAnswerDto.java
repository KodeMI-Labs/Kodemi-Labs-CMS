package com.ContentManagementSystem.CMS.dto.quizDto;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
public class QuizAnswerDto {
    private String answer_id;
    private String attempt_id;
    private String question_id;
    private String selectedOption_id;
    private Boolean correct;
    private LocalDateTime answeredAt;
}
