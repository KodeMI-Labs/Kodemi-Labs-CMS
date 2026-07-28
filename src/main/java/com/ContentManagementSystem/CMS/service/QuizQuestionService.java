package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizQuestionDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizQuestion;

import java.util.List;

public interface QuizQuestionService {
    QuizQuestion createQuizQuestion(QuizQuestionDto quizQuestionDto);
    String createQuizQuestion(QuizQuestion quizQuestion);
    QuizQuestionDto getQuizQuestion(String questionId);
    List<QuizQuestionDto> getAllQuizQuestion();
    List<QuizQuestionDto> getQuestionsByQuizId(String quiz_id);
    String updateQuiz(String questionId,QuizQuestion quizQuestion);
    String delete(String questionId);
}
