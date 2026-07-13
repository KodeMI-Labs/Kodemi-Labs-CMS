package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizOptionDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizOption;

import java.util.List;

public interface QuizOptionService {
    QuizOption createQuizOptionService(QuizOptionDto quizOptionDto);
    String createQuizOptionService(QuizOption quizOption);
    QuizOptionDto getQuizOption(String optionId);
    List<QuizOption> getAllQuizOption();
    List<QuizOption> getOptionsByQuestionId(String questionId);
    String updateQuiz(String optionId,QuizOption quizOption);
    String delete(String optionId);
}
