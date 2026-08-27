package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.quizDto.QuizCategoryDto;
import com.kodemi.model.quizmodel.QuizCategory;

public interface QuizCategoryService {
    QuizCategory createQuizCategory(QuizCategoryDto quizCategoryDto);
    String createQuizCategory(QuizCategory quizCategory);
    QuizCategoryDto getQuizCategory(String categoryId);
    List<QuizCategoryDto> getAllQuizCategory();
    String UpdateQuizCategory(String categoryId,QuizCategory quizCategory);
    String delete(String categoryId);
}
