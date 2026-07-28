package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizAnalyticsDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAnalytics;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAttempt;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAnalyticsRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAttemptRepository;
import com.ContentManagementSystem.CMS.service.impl.QuizAnalyticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizAnalyticsServiceImplTest {

    @Mock
    private QuizAnalyticsRepository quizAnalyticsRepository;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @InjectMocks
    private QuizAnalyticsServiceImpl quizAnalyticsService;

    private QuizAnalytics analytics;

    @BeforeEach
    void setUp() {
        analytics = new QuizAnalytics();
        analytics.setAnalytics_id("a1");
        analytics.setQuiz_id("q1");
        analytics.setQuiz_title("HTML Basics");
        analytics.setTotal_students(30);
        analytics.setAttempted_count(15);
        analytics.setNot_attempted_count(15);
        analytics.setAvg_score_percent(75.0);
        analytics.setHighest_score_percent(92.0);
        analytics.setOverall_performance_percent(75.0);
        analytics.setCompleted_count(10);
        analytics.setIn_progress_count(3);
        analytics.setNot_started_count(2);
        analytics.setRange_90_100_percent(20.0);
        analytics.setRange_80_89_percent(70.0);
        analytics.setRange_70_79_percent(50.0);
        analytics.setRange_60_69_percent(40.0);
        analytics.setRange_50_59_percent(60.0);
    }

    @Test
    void createQuizAnalytics_withDto_returnsAnalytics() {
        QuizAnalyticsDto dto = new QuizAnalyticsDto();
        dto.setQuiz_id("q1");
        dto.setQuiz_title("HTML Basics");
        dto.setTotal_students(30);
        when(quizAnalyticsRepository.save(any(QuizAnalytics.class))).thenAnswer(i -> i.getArgument(0));

        QuizAnalytics result = quizAnalyticsService.createQuizAnalytics(dto);

        assertNotNull(result);
        assertNotNull(result.getAnalytics_id());
        assertEquals("HTML Basics", result.getQuiz_title());
        verify(quizAnalyticsRepository).save(any(QuizAnalytics.class));
    }

    @Test
    void createQuizAnalytics_withModel_returnsSuccessMessage() {
        when(quizAnalyticsRepository.save(any(QuizAnalytics.class))).thenReturn(analytics);

        String result = quizAnalyticsService.createQuizAnalytics(analytics);

        assertEquals("Quiz Analytics Created Successfully", result);
    }

    @Test
    void getQuizAnalyticsById_found_returnsDto() {
        when(quizAnalyticsRepository.findById("a1")).thenReturn(analytics);

        QuizAnalyticsDto result = quizAnalyticsService.getQuizAnalyticsById("a1");

        assertNotNull(result);
        assertEquals("HTML Basics", result.getQuiz_title());
        assertEquals(30, result.getTotal_students());
    }

    @Test
    void getQuizAnalyticsById_notFound_throwsException() {
        when(quizAnalyticsRepository.findById("a1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> quizAnalyticsService.getQuizAnalyticsById("a1"));
    }

    @Test
    void getQuizAnalyticsByQuizId_found_returnsDto() {
        // computeAndSave scans attempts first, then reads analytics
        when(quizAttemptRepository.findAll()).thenReturn(List.of());
        when(quizAnalyticsRepository.findAll()).thenReturn(List.of(analytics));
        when(quizAnalyticsRepository.save(any())).thenReturn(analytics);

        QuizAnalyticsDto result = quizAnalyticsService.getQuizAnalyticsByQuizId("q1");

        assertNotNull(result);
        assertEquals("q1", result.getQuiz_id());
    }

    @Test
    void getQuizAnalyticsByQuizId_notFound_returnsEmptyDto() {
        // When no attempts and no analytics exist, returns empty DTO with zeros (not exception)
        when(quizAttemptRepository.findAll()).thenReturn(List.of());
        when(quizAnalyticsRepository.findAll()).thenReturn(List.of());
        when(quizAnalyticsRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        QuizAnalyticsDto result = quizAnalyticsService.getQuizAnalyticsByQuizId("q1");

        assertNotNull(result);
        assertEquals("q1", result.getQuiz_id());
        assertEquals(0, result.getTotal_students());
    }

    @Test
    void getAllQuizAnalytics_returnsList() {
        // recalculateAllAnalytics scans attempts to find quizIds, then computeAndSave for each
        when(quizAttemptRepository.findAll()).thenReturn(List.of());
        when(quizAnalyticsRepository.findAll()).thenReturn(List.of(analytics));
        when(quizAnalyticsRepository.save(any())).thenReturn(analytics);

        List<QuizAnalyticsDto> result = quizAnalyticsService.getAllQuizAnalytics();

        assertEquals(1, result.size());
        assertEquals("HTML Basics", result.get(0).getQuiz_title());
    }

    @Test
    void updateQuizAnalytics_found_returnsSuccessMessage() {
        when(quizAnalyticsRepository.findById("a1")).thenReturn(analytics);
        when(quizAnalyticsRepository.save(any())).thenReturn(analytics);

        String result = quizAnalyticsService.updateQuizAnalytics("a1", analytics);

        assertEquals("Quiz Analytics Updated Successfully", result);
    }

    @Test
    void updateQuizAnalytics_notFound_throwsException() {
        when(quizAnalyticsRepository.findById("a1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> quizAnalyticsService.updateQuizAnalytics("a1", analytics));
    }

    @Test
    void deleteQuizAnalytics_found_returnsSuccessMessage() {
        when(quizAnalyticsRepository.findById("a1")).thenReturn(analytics);

        String result = quizAnalyticsService.delete("a1");

        assertEquals("Quiz Analytics Deleted Successfully", result);
        verify(quizAnalyticsRepository).delete("a1");
    }

    @Test
    void deleteQuizAnalytics_notFound_throwsException() {
        when(quizAnalyticsRepository.findById("a1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> quizAnalyticsService.delete("a1"));
    }
}
