package com.kodemi.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.quizDto.QuizAttemptDto;
import com.kodemi.enums.AttemptStatus;
import com.kodemi.model.quizmodel.Quiz;
import com.kodemi.model.quizmodel.QuizAnalytics;
import com.kodemi.model.quizmodel.QuizAttempt;
import com.kodemi.model.quizmodel.QuizStudentResult;
import com.kodemi.repository.quizrepository.QuizAnalyticsRepository;
import com.kodemi.repository.quizrepository.QuizAttemptRepository;
import com.kodemi.repository.quizrepository.QuizRepository;
import com.kodemi.repository.quizrepository.QuizStudentResultRepository;
import com.kodemi.service.QuizAttemptService;

@Service
public class QuizAttemptServiceImpl implements QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;
    private final QuizStudentResultRepository quizStudentResultRepository;
    private final QuizAnalyticsRepository quizAnalyticsRepository;

    public QuizAttemptServiceImpl(QuizAttemptRepository quizAttemptRepository,
                                  QuizRepository quizRepository,
                                  QuizStudentResultRepository quizStudentResultRepository,
                                  QuizAnalyticsRepository quizAnalyticsRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizRepository = quizRepository;
        this.quizStudentResultRepository = quizStudentResultRepository;
        this.quizAnalyticsRepository = quizAnalyticsRepository;
    }

    private void incrementQuizAttempts(String quiz_id) {
        if (quiz_id == null) return;
        Quiz quiz = quizRepository.findById(quiz_id);
        if (quiz != null) {
            int current = quiz.getTotalAttempts() == null ? 0 : quiz.getTotalAttempts();
            quiz.setTotalAttempts(current + 1);
            quizRepository.save(quiz);
        }
    }

    private void autoCreateStudentResult(QuizAttempt attempt) {
        if (attempt.getQuiz_id() == null) return;

        Quiz quiz = quizRepository.findById(attempt.getQuiz_id());

        // Upsert: reuse an existing result record for this learner+quiz if one exists,
        // so we don't accumulate duplicate rows on every update.
        QuizStudentResult result = null;
        List<QuizStudentResult> allResults = quizStudentResultRepository.findAll();
        for (QuizStudentResult r : allResults) {
            if (attempt.getQuiz_id().equals(r.getQuiz_id())
                    && attempt.getLearnerId() != null
                    && attempt.getLearnerId().equals(r.getLearner_id())) {
                result = r;
                break;
            }
        }
        if (result == null) {
            result = new QuizStudentResult();
            result.setResult_id(UUID.randomUUID().toString());
        }

        result.setQuiz_id(attempt.getQuiz_id());
        result.setLearner_id(attempt.getLearnerId());
        // Use learner_name if provided, otherwise fall back to learnerId
        String name = (attempt.getLearner_name() != null && !attempt.getLearner_name().isBlank())
                ? attempt.getLearner_name()
                : attempt.getLearnerId();
        result.setLearner_name(name);
        int totalQ = quiz != null && quiz.getTotalQuestion() != null ? quiz.getTotalQuestion() : 0;
        int totalM = quiz != null && quiz.getTotalMarks() != null ? quiz.getTotalMarks() : 0;
        result.setTotal_questions(totalQ);
        result.setTotal_marks(totalM);
        result.setScore(attempt.getScore() != null ? attempt.getScore() : 0);
        result.setPercent(attempt.getAccuracy() != null ? attempt.getAccuracy() : 0.0);
        result.setQuestions_attempted(attempt.getCorrectAnswers() != null && attempt.getWrongAnswers() != null
                ? attempt.getCorrectAnswers() + attempt.getWrongAnswers() : 0);
        result.setTime_taken_minutes(attempt.getTimeSpentMinutes() != null ? attempt.getTimeSpentMinutes() : 0);
        result.setStatus(attempt.getStatus() != null ? attempt.getStatus().name() : "IN_PROGRESS");
        quizStudentResultRepository.save(result);
    }

    private void updateAnalytics(String quiz_id) {
        updateAnalyticsWithAttempt(quiz_id, null);
    }

    /**
     * Recalculates analytics for a quiz.
     * If newAttempt is provided, it is merged into the scan results to avoid
     * eventual consistency gaps (the just-saved attempt may not appear in scan yet).
     */
    private void updateAnalyticsWithAttempt(String quiz_id, QuizAttempt newAttempt) {
        List<QuizAttempt> allAttempts = quizAttemptRepository.findAll();

        // Build a deduplicated list — include the newAttempt, remove stale version if it exists
        List<QuizAttempt> quizAttempts = new ArrayList<>();
        for (QuizAttempt a : allAttempts) {
            if (!quiz_id.equals(a.getQuiz_id())) continue;
            // Skip if this is the same attempt we just saved (we'll add it fresh below)
            if (newAttempt != null && newAttempt.getAttempt_id() != null
                    && newAttempt.getAttempt_id().equals(a.getAttempt_id())) continue;
            quizAttempts.add(a);
        }
        // Always add the freshly saved attempt so it's definitely in the calculation
        if (newAttempt != null) {
            quizAttempts.add(newAttempt);
        }

        int total = quizAttempts.size();
        int completed = 0, inProgress = 0, notStarted = 0;
        double totalScore = 0, highest = 0;
        int r90 = 0, r80 = 0, r70 = 0, r60 = 0, r50 = 0;

        for (QuizAttempt a : quizAttempts) {
            if (AttemptStatus.COMPLETED.equals(a.getStatus())) completed++;
            else if (AttemptStatus.IN_PROGRESS.equals(a.getStatus())) inProgress++;
            else notStarted++;
            double acc = a.getAccuracy() != null ? a.getAccuracy() : 0.0;
            totalScore += acc;
            if (acc > highest) highest = acc;
            if (acc >= 90) r90++;
            else if (acc >= 80) r80++;
            else if (acc >= 70) r70++;
            else if (acc >= 60) r60++;
            else if (acc >= 50) r50++;
        }

        double avg = total > 0 ? Math.round((totalScore / total) * 100.0) / 100.0 : 0.0;

        // Find existing analytics for this quiz
        List<QuizAnalytics> allAnalytics = quizAnalyticsRepository.findAll();
        QuizAnalytics analytics = null;
        for (QuizAnalytics a : allAnalytics) {
            if (quiz_id.equals(a.getQuiz_id())) { analytics = a; break; }
        }
        if (analytics == null) {
            analytics = new QuizAnalytics();
            analytics.setAnalytics_id(UUID.randomUUID().toString());
            analytics.setQuiz_id(quiz_id);
            analytics.setCreated_at(LocalDateTime.now());
        }

        analytics.setTotal_students(total);
        analytics.setAttempted_count(completed + inProgress);
        analytics.setNot_attempted_count(notStarted);
        analytics.setAvg_score_percent(avg);
        analytics.setHighest_score_percent(highest);
        analytics.setOverall_performance_percent(avg);
        analytics.setCompleted_count(completed);
        analytics.setIn_progress_count(inProgress);
        analytics.setNot_started_count(notStarted);
        analytics.setRange_90_100_percent(total > 0 ? (r90 * 100.0 / total) : 0);
        analytics.setRange_80_89_percent(total > 0 ? (r80 * 100.0 / total) : 0);
        analytics.setRange_70_79_percent(total > 0 ? (r70 * 100.0 / total) : 0);
        analytics.setRange_60_69_percent(total > 0 ? (r60 * 100.0 / total) : 0);
        analytics.setRange_50_59_percent(total > 0 ? (r50 * 100.0 / total) : 0);
        analytics.setUpdated_at(LocalDateTime.now());
        quizAnalyticsRepository.save(analytics);
    }

    @Override
    public QuizAttempt createQuizAttempt(QuizAttemptDto quizAttemptDto) {
        QuizAttempt quizAttempt = new QuizAttempt();
        BeanUtils.copyProperties(quizAttemptDto, quizAttempt);
        quizAttempt.setAttempt_id(UUID.randomUUID().toString());
        quizAttempt.setStartedAt(LocalDateTime.now());
        quizAttemptRepository.save(quizAttempt);
        incrementQuizAttempts(quizAttempt.getQuiz_id());
        // Always create a student result record so queries return data immediately.
        // Analytics only makes sense once the attempt is fully scored.
        if (quizAttempt.getQuiz_id() != null) {
            autoCreateStudentResult(quizAttempt);
        }
        if (AttemptStatus.COMPLETED.equals(quizAttempt.getStatus())) {
            updateAnalyticsWithAttempt(quizAttempt.getQuiz_id(), quizAttempt);
        }
        return quizAttempt;
    }

    @Override
    public QuizAttemptDto createQuizAttempt(QuizAttempt quizAttempt) {
        quizAttempt.setAttempt_id(UUID.randomUUID().toString());
        quizAttempt.setStartedAt(LocalDateTime.now());
        quizAttemptRepository.save(quizAttempt);
        incrementQuizAttempts(quizAttempt.getQuiz_id());
        // Always create a student result record so queries return data immediately.
        if (quizAttempt.getQuiz_id() != null) {
            autoCreateStudentResult(quizAttempt);
        }
        if (AttemptStatus.COMPLETED.equals(quizAttempt.getStatus())) {
            updateAnalyticsWithAttempt(quizAttempt.getQuiz_id(), quizAttempt);
        }
        QuizAttemptDto quizAttemptDto = new QuizAttemptDto();
        BeanUtils.copyProperties(quizAttempt, quizAttemptDto);
        return quizAttemptDto;
    }

    @Override
    public QuizAttemptDto getQuizAttemptId(String attempt_id) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(attempt_id);
        if (quizAttempt == null) {
            throw new ResourceNotFoundException("Quiz Attempt not found with id: " + attempt_id);
        }
        QuizAttemptDto quizAttemptDto = new QuizAttemptDto();
        BeanUtils.copyProperties(quizAttempt, quizAttemptDto);
        return quizAttemptDto;
    }

    @Override
    public List<QuizAttemptDto> getAllQuizAttempt() {
        List<QuizAttempt> attemptList = quizAttemptRepository.findAll();
        List<QuizAttemptDto> attemptDtoList = new ArrayList<>();
        for (QuizAttempt quizAttempt : attemptList) {
            QuizAttemptDto quizAttemptDto = new QuizAttemptDto();
            BeanUtils.copyProperties(quizAttempt, quizAttemptDto);
            attemptDtoList.add(quizAttemptDto);
        }
        return attemptDtoList;
    }

    @Override
    public List<QuizAttemptDto> getAttemptsByLearnerId(String learnerId) {
        List<QuizAttempt> all = quizAttemptRepository.findAll();
        List<QuizAttemptDto> result = new ArrayList<>();
        for (QuizAttempt attempt : all) {
            if (learnerId.equals(attempt.getLearnerId())) {
                QuizAttemptDto dto = new QuizAttemptDto();
                BeanUtils.copyProperties(attempt, dto);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<QuizAttemptDto> getAttemptsByQuizId(String quiz_id) {
        List<QuizAttempt> all = quizAttemptRepository.findAll();
        List<QuizAttemptDto> result = new ArrayList<>();
        for (QuizAttempt attempt : all) {
            if (quiz_id.equals(attempt.getQuiz_id())) {
                QuizAttemptDto dto = new QuizAttemptDto();
                BeanUtils.copyProperties(attempt, dto);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public QuizAttemptDto UpdateQuizAttempt(String attempt_id, QuizAttempt quizAttempt) {
        QuizAttempt existing = quizAttemptRepository.findById(attempt_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Quiz Attempt not found with id: " + attempt_id);
        }

        existing.setStatus(quizAttempt.getStatus());
        existing.setSubmittedAt(quizAttempt.getSubmittedAt() != null ? quizAttempt.getSubmittedAt() : LocalDateTime.now());
        existing.setTimeSpentMinutes(quizAttempt.getTimeSpentMinutes());
        if (quizAttempt.getLearnerId() != null && !quizAttempt.getLearnerId().isBlank()) {
            existing.setLearnerId(quizAttempt.getLearnerId());
        }
        if (quizAttempt.getLearner_name() != null && !quizAttempt.getLearner_name().isBlank()) {
            existing.setLearner_name(quizAttempt.getLearner_name());
        }

        // Auto-calculate score, wrongAnswers, accuracy from correctAnswers + totalQuestions
        int correctAnswers = quizAttempt.getCorrectAnswers() != null ? quizAttempt.getCorrectAnswers() : 0;
        existing.setCorrectAnswers(correctAnswers);

        // Get total questions from Quiz table
        Quiz quiz = existing.getQuiz_id() != null ? quizRepository.findById(existing.getQuiz_id()) : null;
        int totalQuestions = (quiz != null && quiz.getTotalQuestion() != null) ? quiz.getTotalQuestion() : 0;

        // If frontend passed score directly, use it; otherwise calculate from correct answers
        if (quizAttempt.getScore() != null) {
            existing.setScore(quizAttempt.getScore());
        } else if (totalQuestions > 0) {
            // score = correctAnswers (1 mark per question)
            existing.setScore(correctAnswers);
        }

        // wrongAnswers = totalQuestions - correctAnswers 
        int wrongAnswers = quizAttempt.getWrongAnswers() != null
                ? quizAttempt.getWrongAnswers()
                : Math.max(0, totalQuestions - correctAnswers);
        existing.setWrongAnswers(wrongAnswers);

        // accuracy = (correctAnswers / totalQuestions) * 100
        if (quizAttempt.getAccuracy() != null) {
            existing.setAccuracy(quizAttempt.getAccuracy());
        } else if (totalQuestions > 0) {
            double accuracy = Math.round((correctAnswers * 100.0 / totalQuestions) * 100.0) / 100.0;
            existing.setAccuracy(accuracy);
        } else {
            existing.setAccuracy(0.0);
        }

        quizAttemptRepository.save(existing);
        
        // Always sync the student result with the latest attempt data
        if (existing.getQuiz_id() != null) {
            autoCreateStudentResult(existing);
        }
        // Trigger analytics only when the attempt is completed
        if (AttemptStatus.COMPLETED.equals(existing.getStatus())) {
            updateAnalyticsWithAttempt(existing.getQuiz_id(), existing);
        }
        
        QuizAttemptDto dto = new QuizAttemptDto();
        BeanUtils.copyProperties(existing, dto);
        return dto;
    }

    @Override
    public QuizAttemptDto patchQuizAttempt(String attempt_id, QuizAttemptDto patchDto) {
        QuizAttempt existing = quizAttemptRepository.findById(attempt_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Quiz Attempt not found with id: " + attempt_id);
        }

        // Only update fields that are currently null OR explicitly provided in the patch
        // This is safe for fixing existing records with null data
        
        if (patchDto.getLearnerId() != null && !patchDto.getLearnerId().isBlank()) {
            existing.setLearnerId(patchDto.getLearnerId());
        }
        
        if (patchDto.getLearner_name() != null && !patchDto.getLearner_name().isBlank()) {
            existing.setLearner_name(patchDto.getLearner_name());
        }
        
        if (patchDto.getQuiz_id() != null && !patchDto.getQuiz_id().isBlank()) {
            existing.setQuiz_id(patchDto.getQuiz_id());
        }
        
        if (patchDto.getStatus() != null) {
            existing.setStatus(patchDto.getStatus());
        }
        
        if (patchDto.getScore() != null) {
            existing.setScore(patchDto.getScore());
        }
        
        if (patchDto.getCorrectAnswers() != null) {
            existing.setCorrectAnswers(patchDto.getCorrectAnswers());
        }
        
        if (patchDto.getWrongAnswers() != null) {
            existing.setWrongAnswers(patchDto.getWrongAnswers());
        }
        
        if (patchDto.getAccuracy() != null) {
            existing.setAccuracy(patchDto.getAccuracy());
        }
        
        if (patchDto.getTimeSpentMinutes() != null) {
            existing.setTimeSpentMinutes(patchDto.getTimeSpentMinutes());
        }
        
        if (patchDto.getSubmittedAt() != null) {
            existing.setSubmittedAt(patchDto.getSubmittedAt());
        }

        quizAttemptRepository.save(existing);

        // Always sync the student result with the latest attempt data
        if (existing.getQuiz_id() != null) {
            autoCreateStudentResult(existing);
        }
        // Trigger analytics only when the attempt is completed
        if (AttemptStatus.COMPLETED.equals(existing.getStatus())) {
            updateAnalyticsWithAttempt(existing.getQuiz_id(), existing);
        }

        QuizAttemptDto dto = new QuizAttemptDto();
        BeanUtils.copyProperties(existing, dto);
        return dto;
    }

    @Override
    public String delete(String attempt_id) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(attempt_id);
        if (quizAttempt == null) {
            throw new ResourceNotFoundException("Quiz Attempt not found with id: " + attempt_id);
        }
        quizAttemptRepository.delete(attempt_id);
        return "Quiz Attempt Deleted Successfully";
    }
}
