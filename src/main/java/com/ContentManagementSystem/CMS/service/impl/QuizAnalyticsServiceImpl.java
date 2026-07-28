package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizAnalyticsDto;
import com.ContentManagementSystem.CMS.enums.AttemptStatus;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAnalytics;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAttempt;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAnalyticsRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAttemptRepository;
import com.ContentManagementSystem.CMS.service.QuizAnalyticsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class QuizAnalyticsServiceImpl implements QuizAnalyticsService {

    private final QuizAnalyticsRepository quizAnalyticsRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    public QuizAnalyticsServiceImpl(QuizAnalyticsRepository quizAnalyticsRepository,
                                    QuizAttemptRepository quizAttemptRepository) {
        this.quizAnalyticsRepository = quizAnalyticsRepository;
        this.quizAttemptRepository = quizAttemptRepository;
    }

    // ─── Recalculate for one quiz ─────────────────────────────────────────────

    @Override
    public String recalculateAnalytics(String quiz_id) {
        computeAndSave(quiz_id);
        return "Analytics recalculated successfully for quiz: " + quiz_id;
    }

    // ─── Recalculate for ALL quizzes (one-time backfill) ─────────────────────

    @Override
    public String recalculateAllAnalytics() {
        List<QuizAttempt> allAttempts = quizAttemptRepository.findAll();
        Set<String> quizIds = new HashSet<>();
        for (QuizAttempt a : allAttempts) {
            if (a.getQuiz_id() != null) quizIds.add(a.getQuiz_id());
        }
        List<QuizAnalytics> existingAnalytics = quizAnalyticsRepository.findAll();
        for (QuizAnalytics a : existingAnalytics) {
            if (a.getQuiz_id() != null) quizIds.add(a.getQuiz_id());
        }
        for (String quizId : quizIds) {
            computeAndSave(quizId);
        }
        return "Analytics recalculated for " + quizIds.size() + " quizzes";
    }

    // ─── Core calculation logic ───────────────────────────────────────────────

    private void computeAndSave(String quiz_id) {
        List<QuizAttempt> allAttempts = quizAttemptRepository.findAll();
        List<QuizAttempt> quizAttempts = new ArrayList<>();
        for (QuizAttempt a : allAttempts) {
            if (quiz_id.equals(a.getQuiz_id())) quizAttempts.add(a);
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

    // ─── Existing CRUD methods ────────────────────────────────────────────────

    @Override
    public QuizAnalytics createQuizAnalytics(QuizAnalyticsDto dto) {
        QuizAnalytics analytics = new QuizAnalytics();
        BeanUtils.copyProperties(dto, analytics);
        analytics.setAnalytics_id(UUID.randomUUID().toString());
        analytics.setCreated_at(LocalDateTime.now());
        analytics.setUpdated_at(LocalDateTime.now());
        quizAnalyticsRepository.save(analytics);
        return analytics;
    }

    @Override
    public String createQuizAnalytics(QuizAnalytics quizAnalytics) {
        quizAnalytics.setAnalytics_id(UUID.randomUUID().toString());
        quizAnalytics.setCreated_at(LocalDateTime.now());
        quizAnalytics.setUpdated_at(LocalDateTime.now());
        quizAnalyticsRepository.save(quizAnalytics);
        return "Quiz Analytics Created Successfully";
    }

    @Override
    public QuizAnalyticsDto getQuizAnalyticsById(String analytics_id) {
        QuizAnalytics analytics = quizAnalyticsRepository.findById(analytics_id);
        if (analytics == null) {
            throw new ResourceNotFoundException("Quiz Analytics not found with id: " + analytics_id);
        }
        QuizAnalyticsDto dto = new QuizAnalyticsDto();
        BeanUtils.copyProperties(analytics, dto);
        return dto;
    }

    @Override
    public QuizAnalyticsDto getQuizAnalyticsByQuizId(String quiz_id) {
        // Always recalculate fresh from attempts — eliminates intermittent stale data
        computeAndSave(quiz_id);

        List<QuizAnalytics> all = quizAnalyticsRepository.findAll();
        for (QuizAnalytics analytics : all) {
            if (quiz_id.equals(analytics.getQuiz_id())) {
                QuizAnalyticsDto dto = new QuizAnalyticsDto();
                BeanUtils.copyProperties(analytics, dto);
                return dto;
            }
        }
        // If no attempts exist yet, return an empty analytics object with zeros
        QuizAnalyticsDto empty = new QuizAnalyticsDto();
        empty.setQuiz_id(quiz_id);
        empty.setTotal_students(0);
        empty.setAttempted_count(0);
        empty.setNot_attempted_count(0);
        empty.setAvg_score_percent(0.0);
        empty.setHighest_score_percent(0.0);
        empty.setOverall_performance_percent(0.0);
        empty.setCompleted_count(0);
        empty.setIn_progress_count(0);
        empty.setNot_started_count(0);
        empty.setRange_90_100_percent(0.0);
        empty.setRange_80_89_percent(0.0);
        empty.setRange_70_79_percent(0.0);
        empty.setRange_60_69_percent(0.0);
        empty.setRange_50_59_percent(0.0);
        return empty;
    }

    @Override
    public List<QuizAnalyticsDto> getAllQuizAnalytics() {
        // Always recalculate all quizzes fresh before returning
        recalculateAllAnalytics();

        List<QuizAnalytics> list = quizAnalyticsRepository.findAll();
        List<QuizAnalyticsDto> dtoList = new ArrayList<>();
        for (QuizAnalytics analytics : list) {
            QuizAnalyticsDto dto = new QuizAnalyticsDto();
            BeanUtils.copyProperties(analytics, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public String updateQuizAnalytics(String analytics_id, QuizAnalytics quizAnalytics) {
        QuizAnalytics existing = quizAnalyticsRepository.findById(analytics_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Quiz Analytics not found with id: " + analytics_id);
        }
        existing.setQuiz_title(quizAnalytics.getQuiz_title());
        existing.setTotal_students(quizAnalytics.getTotal_students());
        existing.setAttempted_count(quizAnalytics.getAttempted_count());
        existing.setNot_attempted_count(quizAnalytics.getNot_attempted_count());
        existing.setAvg_score_percent(quizAnalytics.getAvg_score_percent());
        existing.setHighest_score_percent(quizAnalytics.getHighest_score_percent());
        existing.setOverall_performance_percent(quizAnalytics.getOverall_performance_percent());
        existing.setCompleted_count(quizAnalytics.getCompleted_count());
        existing.setIn_progress_count(quizAnalytics.getIn_progress_count());
        existing.setNot_started_count(quizAnalytics.getNot_started_count());
        existing.setRange_90_100_percent(quizAnalytics.getRange_90_100_percent());
        existing.setRange_80_89_percent(quizAnalytics.getRange_80_89_percent());
        existing.setRange_70_79_percent(quizAnalytics.getRange_70_79_percent());
        existing.setRange_60_69_percent(quizAnalytics.getRange_60_69_percent());
        existing.setRange_50_59_percent(quizAnalytics.getRange_50_59_percent());
        existing.setUpdated_at(LocalDateTime.now());
        quizAnalyticsRepository.save(existing);
        return "Quiz Analytics Updated Successfully";
    }

    @Override
    public String delete(String analytics_id) {
        QuizAnalytics analytics = quizAnalyticsRepository.findById(analytics_id);
        if (analytics == null) {
            throw new ResourceNotFoundException("Quiz Analytics not found with id: " + analytics_id);
        }
        quizAnalyticsRepository.delete(analytics_id);
        return "Quiz Analytics Deleted Successfully";
    }
}
