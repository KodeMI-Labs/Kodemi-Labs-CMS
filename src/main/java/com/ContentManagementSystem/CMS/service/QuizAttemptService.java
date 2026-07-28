package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizAttemptDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAttempt;

import java.util.List;

public interface QuizAttemptService {
    QuizAttempt createQuizAttempt(QuizAttemptDto quizAttemptDto);
    QuizAttemptDto createQuizAttempt(QuizAttempt quizAttempt);
    QuizAttemptDto getQuizAttemptId(String attempt_id);
    List<QuizAttemptDto> getAllQuizAttempt();
    List<QuizAttemptDto> getAttemptsByLearnerId(String learnerId);
    List<QuizAttemptDto> getAttemptsByQuizId(String quiz_id);
    QuizAttemptDto UpdateQuizAttempt(String attempt_id, QuizAttempt quizAttempt);
    QuizAttemptDto patchQuizAttempt(String attempt_id, QuizAttemptDto patchDto);
    String delete(String attempt_id);
}
