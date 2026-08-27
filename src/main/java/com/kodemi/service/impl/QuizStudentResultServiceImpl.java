package com.kodemi.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.client.UserServiceClient;
import com.kodemi.dto.reviewsandratings.quizDto.LearnerResponseDTO;
import com.kodemi.dto.reviewsandratings.quizDto.QuizStudentResultDto;
import com.kodemi.model.quizmodel.Quiz;
import com.kodemi.model.quizmodel.QuizAttempt;
import com.kodemi.model.quizmodel.QuizStudentResult;
import com.kodemi.repository.quizrepository.QuizAttemptRepository;
import com.kodemi.repository.quizrepository.QuizRepository;
import com.kodemi.repository.quizrepository.QuizStudentResultRepository;
import com.kodemi.service.QuizStudentResultService;

@Service
public class QuizStudentResultServiceImpl implements QuizStudentResultService {

    private final QuizStudentResultRepository quizStudentResultRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;
    private final UserServiceClient userServiceClient;

    public QuizStudentResultServiceImpl(QuizStudentResultRepository quizStudentResultRepository,
                                        QuizAttemptRepository quizAttemptRepository,
                                        QuizRepository quizRepository,UserServiceClient userServiceClient) {
        this.quizStudentResultRepository = quizStudentResultRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.userServiceClient=userServiceClient;
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
        dto.setLearner_name(resolveLearnerName(result.getLearner_id()));
        return dto;
    }

    @Override
    public List<QuizStudentResultDto> getAllResults() {
        List<QuizStudentResult> list = quizStudentResultRepository.findAll();
        List<QuizStudentResultDto> dtoList = new ArrayList<>();        
        for (QuizStudentResult result : list) {
            QuizStudentResultDto dto = new QuizStudentResultDto();
            BeanUtils.copyProperties(result, dto);
            dto.setLearner_name(resolveLearnerName(result.getLearner_id()));
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
                dto.setLearner_name(resolveLearnerName(result.getLearner_id()));
            }
        }
        return dtoList;
    }

    @Override
    public List<QuizStudentResultDto> getResultsByLearnerId(String learner_id) {
        List<QuizStudentResult> all = quizStudentResultRepository.findAll();
        List<QuizStudentResultDto> dtoList = new ArrayList<>();
        String resolvedName = resolveLearnerName(learner_id);
        for (QuizStudentResult result : all) {
            if (learner_id.equals(result.getLearner_id())) {
                QuizStudentResultDto dto = new QuizStudentResultDto();
                BeanUtils.copyProperties(result, dto);
                dto.setLearner_name(resolvedName);
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
    public String backfillStudentResults() {
        List<QuizAttempt> allAttempts = quizAttemptRepository.findAll();
        List<QuizStudentResult> existingResults = quizStudentResultRepository.findAll();

        Set<String> alreadyExists = new HashSet<>();
        for (QuizStudentResult r : existingResults) {
            if (r.getLearner_id() != null && r.getQuiz_id() != null) {
                alreadyExists.add(r.getLearner_id() + "::" + r.getQuiz_id());
            }
        }

        int created = 0;
        for (QuizAttempt attempt : allAttempts) {
            String key = attempt.getLearnerId() + "::" + attempt.getQuiz_id();
            if (alreadyExists.contains(key)) continue;

            Quiz quiz = attempt.getQuiz_id() != null ? quizRepository.findById(attempt.getQuiz_id()) : null;

            QuizStudentResult result = new QuizStudentResult();
            result.setResult_id(UUID.randomUUID().toString());
            result.setQuiz_id(attempt.getQuiz_id());
            result.setLearner_id(attempt.getLearnerId());

            // Fetch name from user-service, fall back to attempt learner_name or learnerId
            String name = resolveLearnerName(attempt.getLearnerId());
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
            alreadyExists.add(key);
            created++;
        }

        return "Backfill complete. Created " + created + " student result records.";
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

 // Helper method to fetch the learner name from user-service via Feign
    private String resolveLearnerName(String learnerId) {
        if (learnerId == null || learnerId.isBlank()) {
            return "Unknown Learner";
        }
        try {
            LearnerResponseDTO learner = userServiceClient.getLearnerById(learnerId);
            if (learner != null && learner.getFullName() != null && !learner.getFullName().isBlank()) {
                return learner.getFullName();
            }
            if (learner != null && learner.getUsername() != null && !learner.getUsername().isBlank()) {
                return learner.getUsername();
            }
        } catch (Exception e) {
            // Log fallback in case user-service is unreachable or learner is not found
            System.err.println("Failed to fetch learner name for ID: " + learnerId + ". Error: " + e.getMessage());
        }
        return learnerId; // Fallback to learnerId if name cannot be retrieved
    }
}
