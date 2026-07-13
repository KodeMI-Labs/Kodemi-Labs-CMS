package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizStudentResultDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizStudentResult;

import java.util.List;

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
