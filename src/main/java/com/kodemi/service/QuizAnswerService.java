package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.quizDto.QuizAnswerDto;
import com.kodemi.model.quizmodel.QuizAnswer;

public interface QuizAnswerService {
    QuizAnswer createQuizAnswer(QuizAnswerDto quizAnswerDto);
    String createQuizAnswer(QuizAnswer quizAnswer);
    QuizAnswerDto getQuizAnswerId(String answer_id);
    List<QuizAnswerDto> getAllQuizAnswer();
    List<QuizAnswerDto> getAnswersByAttemptId(String attempt_id);
    List<QuizAnswerDto> getAnswersByQuestionId(String question_id);
    String UpdateQuizAnswer(String answer_id,QuizAnswer quizAnswer);
    String delete(String answer_id);
}
