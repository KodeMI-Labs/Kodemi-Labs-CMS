package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.Quiz;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAnalyticsRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizRepository;
import com.ContentManagementSystem.CMS.service.impl.QuizServiceImpl;
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
class QuizServiceImplTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizAnalyticsRepository quizAnalyticsRepository;

    @InjectMocks
    private QuizServiceImpl quizService;

    private Quiz quiz;

    @BeforeEach
    void setUp() {
        quiz = new Quiz();
        quiz.setQuiz_id("q1");
        quiz.setTitle("Spring Boot Quiz");
        quiz.setCategory("Tech");
    }

    @Test
    void createQuiz_withDto_returnsQuiz() {
        QuizDto dto = new QuizDto();
        dto.setTitle("Spring Boot Quiz");
        dto.setCategory("Tech");
        when(quizRepository.save(any(Quiz.class))).thenAnswer(i -> i.getArgument(0));
        when(quizAnalyticsRepository.save(any())).thenAnswer(i -> i.getArgument(0)); // createDefaultAnalytics

        Quiz result = quizService.createQuiz(dto);

        assertNotNull(result);
        assertEquals("Spring Boot Quiz", result.getTitle());
        verify(quizRepository).save(any(Quiz.class));
    }

    @Test
    void createQuiz_withModel_returnsSuccessMessage() {
        when(quizRepository.save(quiz)).thenReturn(quiz);
        when(quizAnalyticsRepository.save(any())).thenAnswer(i -> i.getArgument(0)); // createDefaultAnalytics

        String result = quizService.createQuiz(quiz);

        assertEquals("Quiz Created Successfully", result);
    }

    @Test
    void findQuizId_found_returnsDto() {
        when(quizRepository.findById("q1")).thenReturn(quiz);

        QuizDto result = quizService.findQuizId("q1");

        assertNotNull(result);
        assertEquals("Spring Boot Quiz", result.getTitle());
    }

    @Test
    void findQuizId_notFound_throwsException() {
        when(quizRepository.findById("q1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizService.findQuizId("q1"));
    }

    @Test
    void getAllQuiz_returnsList() {
        when(quizRepository.findAll()).thenReturn(List.of(quiz));

        List<QuizDto> result = quizService.getAllQuiz();

        assertEquals(1, result.size());
    }

    @Test
    void updateQuiz_found_returnsSuccessMessage() {
        when(quizRepository.findById("q1")).thenReturn(quiz);
        when(quizRepository.save(any())).thenReturn(quiz);

        String result = quizService.UpdateQuiz("q1", quiz);

        assertEquals("Quiz Updated Successfully", result);
    }

    @Test
    void updateQuiz_notFound_throwsException() {
        when(quizRepository.findById("q1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizService.UpdateQuiz("q1", quiz));
    }

    @Test
    void deleteQuiz_found_returnsSuccessMessage() {
        when(quizRepository.findById("q1")).thenReturn(quiz);

        String result = quizService.delete("q1");

        assertEquals("Quiz Deleted Successfully", result);
        verify(quizRepository).delete("q1");
    }

    @Test
    void deleteQuiz_notFound_throwsException() {
        when(quizRepository.findById("q1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizService.delete("q1"));
    }
}
