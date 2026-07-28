package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizDto;
import com.ContentManagementSystem.CMS.model.quizmodel.Quiz;

import java.util.List;

public interface QuizService {
    Quiz createQuiz(QuizDto quizDto);
    String createQuiz(Quiz quiz);
    QuizDto findQuizId(String quiz_id);
    List<QuizDto> getAllQuiz();
    String UpdateQuiz(String quiz_id,Quiz quiz);
    String delete(String quiz_id);
}
