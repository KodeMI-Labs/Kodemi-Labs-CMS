package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.quizDto.QuizStudentResultDto;
import com.kodemi.model.quizmodel.QuizStudentResult;

public interface QuizStudentResultService {
    QuizStudentResult createResult(QuizStudentResultDto dto);
    String createResult(QuizStudentResult result);
    QuizStudentResultDto getResultById(String result_id);
    List<QuizStudentResultDto> getAllResults();
    List<QuizStudentResultDto> getResultsByQuizId(String quiz_id);
    List<QuizStudentResultDto> getResultsByLearnerId(String learner_id);
    String updateResult(String result_id, QuizStudentResult result);
    String delete(String result_id);

    /** Backfills student results for all existing attempts that don't have one yet */
    String backfillStudentResults();
}
