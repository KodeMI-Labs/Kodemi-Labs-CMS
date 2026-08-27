package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.quizDto.QuizQuestionDto;
import com.kodemi.model.quizmodel.QuizQuestion;

public interface QuizQuestionService {
    QuizQuestion createQuizQuestion(QuizQuestionDto quizQuestionDto);
    String createQuizQuestion(QuizQuestion quizQuestion);
    QuizQuestionDto getQuizQuestion(String questionId);
    List<QuizQuestionDto> getAllQuizQuestion();
    List<QuizQuestionDto> getQuestionsByQuizId(String quiz_id);
    String updateQuiz(String questionId,QuizQuestion quizQuestion);
    String delete(String questionId);
}
