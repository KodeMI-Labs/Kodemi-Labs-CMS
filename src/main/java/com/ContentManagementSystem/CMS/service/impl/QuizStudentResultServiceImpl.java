package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizStudentResultDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.Quiz;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAttempt;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizStudentResult;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAttemptRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizStudentResultRepository;
import com.ContentManagementSystem.CMS.service.QuizStudentResultService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class QuizStudentResultServiceImpl implements QuizStudentResultService {

    private final QuizStudentResultRepository quizStudentResultRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;

    public QuizStudentResultServiceImpl(QuizStudentResultRepository quizStudentResultRepository,
                                        QuizAttemptRepository quizAttemptRepository,
                                        QuizRepository quizRepository) {
        this.quizStudentResultRepository = quizStudentResultRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public QuizStudentResult createResult(QuizStudentResultDto dto) {
        QuizStudentResult result = new QuizStudentResult();
        BeanUtils.copyProperties(dto, result);
        result.setResult_id(UUID.randomUUID().toString());
        quizStudentResultRepository.save(result);
        return result;
    }

    @Override
    public String createResult(QuizStudentResult result) {
        result.setResult_id(UUID.randomUUID().toString());
        quizStudentResultRepository.save(result);
        return "Quiz Student Result Created Successfully";
    }

    @Override
    public QuizStudentResultDto getResultById(String result_id) {
        QuizStudentResult result = quizStudentResultRepository.findById(result_id);
        if (result == null) {
            throw new ResourceNotFoundException("Quiz Student Result not found with id: " + result_id);
        }
        QuizStudentResultDto dto = new QuizStudentResultDto();
        BeanUtils.copyProperties(result, dto);
        return dto;
    }

    @Override
    public List<QuizStudentResultDto> getAllResults() {
        List<QuizStudentResult> list = quizStudentResultRepository.findAll();
        List<QuizStudentResultDto> dtoList = new ArrayList<>();
        for (QuizStudentResult result : list) {
            QuizStudentResultDto dto = new QuizStudentResultDto();
            BeanUtils.copyProperties(result, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<QuizStudentResultDto> getResultsByQuizId(String quiz_id) {
        List<QuizStudentResult> all = quizStudentResultRepository.findAll();
        List<QuizStudentResultDto> dtoList = new ArrayList<>();
        for (QuizStudentResult result : all) {
            if (quiz_id.equals(result.getQuiz_id())) {
                QuizStudentResultDto dto = new QuizStudentResultDto();
                BeanUtils.copyProperties(result, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public List<QuizStudentResultDto> getResultsByLearnerId(String learner_id) {
        List<QuizStudentResult> all = quizStudentResultRepository.findAll();
        List<QuizStudentResultDto> dtoList = new ArrayList<>();
        for (QuizStudentResult result : all) {
            if (learner_id.equals(result.getLearner_id())) {
                QuizStudentResultDto dto = new QuizStudentResultDto();
                BeanUtils.copyProperties(result, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public String updateResult(String result_id, QuizStudentResult result) {
        QuizStudentResult existing = quizStudentResultRepository.findById(result_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Quiz Student Result not found with id: " + result_id);
        }
        existing.setLearner_name(result.getLearner_name());
        existing.setQuestions_attempted(result.getQuestions_attempted());
        existing.setScore(result.getScore());
        existing.setPercent(result.getPercent());
        existing.setTime_taken_minutes(result.getTime_taken_minutes());
        existing.setStatus(result.getStatus());
        quizStudentResultRepository.save(existing);
        return "Quiz Student Result Updated Successfully";
    }

    @Override
    public String delete(String result_id) {
        QuizStudentResult result = quizStudentResultRepository.findById(result_id);
        if (result == null) {
            throw new ResourceNotFoundException("Quiz Student Result not found with id: " + result_id);
        }
        quizStudentResultRepository.delete(result_id);
        return "Quiz Student Result Deleted Successfully";
    }

    /**
     * Backfills QuizStudentResult records for all existing QuizAttempts that
     * don't have a corresponding result yet. Identifies duplicates by learnerId+quizId.
     */
    @Override
    public String backfillStudentResults() {
        List<QuizAttempt> allAttempts = quizAttemptRepository.findAll();
        List<QuizStudentResult> existingResults = quizStudentResultRepository.findAll();

        // Build a set of "learner_id::quiz_id" that already have results
        Set<String> alreadyExists = new HashSet<>();
        for (QuizStudentResult r : existingResults) {
            if (r.getLearner_id() != null && r.getQuiz_id() != null) {
                alreadyExists.add(r.getLearner_id() + "::" + r.getQuiz_id());
            }
        }

        int created = 0;
        for (QuizAttempt attempt : allAttempts) {
            String key = attempt.getLearnerId() + "::" + attempt.getQuiz_id();
            if (alreadyExists.contains(key)) continue; // already has result

            Quiz quiz = attempt.getQuiz_id() != null ? quizRepository.findById(attempt.getQuiz_id()) : null;

            QuizStudentResult result = new QuizStudentResult();
            result.setResult_id(UUID.randomUUID().toString());
            result.setQuiz_id(attempt.getQuiz_id());
            result.setLearner_id(attempt.getLearnerId());
            // Use learner_name if stored on attempt, otherwise fall back to learnerId
            String name = (attempt.getLearner_name() != null && !attempt.getLearner_name().isBlank())
                    ? attempt.getLearner_name()
                    : attempt.getLearnerId();
            result.setLearner_name(name);
            result.setTotal_questions(quiz != null && quiz.getTotalQuestion() != null ? quiz.getTotalQuestion() : 0);
            result.setTotal_marks(quiz != null && quiz.getTotalMarks() != null ? quiz.getTotalMarks() : 0);
            result.setScore(attempt.getScore() != null ? attempt.getScore() : 0);
            result.setPercent(attempt.getAccuracy() != null ? attempt.getAccuracy() : 0.0);
            result.setQuestions_attempted(
                    attempt.getCorrectAnswers() != null && attempt.getWrongAnswers() != null
                            ? attempt.getCorrectAnswers() + attempt.getWrongAnswers() : 0);
            result.setTime_taken_minutes(attempt.getTimeSpentMinutes() != null ? attempt.getTimeSpentMinutes() : 0);
            result.setStatus(attempt.getStatus() != null ? attempt.getStatus().name() : "COMPLETED");

            quizStudentResultRepository.save(result);
            alreadyExists.add(key); // prevent duplicates within same batch
            created++;
        }

        return "Backfill complete. Created " + created + " student result records.";
    }
}
