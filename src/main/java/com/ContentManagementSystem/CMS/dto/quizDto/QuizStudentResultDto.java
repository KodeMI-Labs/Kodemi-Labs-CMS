package com.ContentManagementSystem.CMS.dto.quizDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizStudentResultDto {

    private String result_id;
    private String quiz_id;
    private String learner_id;
    private String learner_name;
    private Integer questions_attempted;
    private Integer total_questions;
    private Integer score;
    private Integer total_marks;
    private Double percent;
    private Integer time_taken_minutes;
    private String status;
}
