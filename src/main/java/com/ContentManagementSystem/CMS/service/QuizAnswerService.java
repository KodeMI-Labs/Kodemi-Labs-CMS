package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizAnswerDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAnswer;

import java.util.List;

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
