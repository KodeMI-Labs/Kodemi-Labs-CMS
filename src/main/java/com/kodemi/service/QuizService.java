package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.quizDto.QuizDto;
import com.kodemi.model.quizmodel.Quiz;

public interface QuizService {
    Quiz createQuiz(QuizDto quizDto);
    String createQuiz(Quiz quiz);
    QuizDto findQuizId(String quiz_id);
    List<QuizDto> getAllQuiz();
    String UpdateQuiz(String quiz_id,Quiz quiz);
    String delete(String quiz_id);
  
    List<QuizDto> getQuizzesByTrainer(String trainer_id);
}
