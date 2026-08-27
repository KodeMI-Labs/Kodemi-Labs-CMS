package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.quizDto.QuizOptionDto;
import com.kodemi.model.quizmodel.QuizOption;

public interface QuizOptionService {
    QuizOption createQuizOptionService(QuizOptionDto quizOptionDto);
    String createQuizOptionService(QuizOption quizOption);
    QuizOptionDto getQuizOption(String optionId);
    List<QuizOption> getAllQuizOption();
    List<QuizOption> getOptionsByQuestionId(String questionId);
    String updateQuiz(String optionId,QuizOption quizOption);
    String delete(String optionId);
}
