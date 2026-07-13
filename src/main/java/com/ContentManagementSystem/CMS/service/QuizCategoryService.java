package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizCategoryDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizCategory;

import java.util.List;

public interface QuizCategoryService {
    QuizCategory createQuizCategory(QuizCategoryDto quizCategoryDto);
    String createQuizCategory(QuizCategory quizCategory);
    QuizCategoryDto getQuizCategory(String categoryId);
    List<QuizCategoryDto> getAllQuizCategory();
    String UpdateQuizCategory(String categoryId,QuizCategory quizCategory);
    String delete(String categoryId);
}
