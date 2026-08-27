package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.reviewsandratings.quizDto.QuizAnalyticsDto;
import com.kodemi.model.quizmodel.QuizAnalytics;

public interface QuizAnalyticsService {
    QuizAnalytics createQuizAnalytics(QuizAnalyticsDto quizAnalyticsDto);
    String createQuizAnalytics(QuizAnalytics quizAnalytics);
    QuizAnalyticsDto getQuizAnalyticsById(String analytics_id);
    QuizAnalyticsDto getQuizAnalyticsByQuizId(String quiz_id);
    List<QuizAnalyticsDto> getAllQuizAnalytics();
    String updateQuizAnalytics(String analytics_id, QuizAnalytics quizAnalytics);
    String delete(String analytics_id);

    /** Recalculates analytics from scratch using all existing attempts for the quiz */
    String recalculateAnalytics(String quiz_id);

    /** Recalculates analytics for ALL quizzes — useful for one-time backfill */
    String recalculateAllAnalytics();
}
