package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizAnalyticsDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAnalytics;

import java.util.List;

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
