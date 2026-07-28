package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizAttemptDto;
import com.ContentManagementSystem.CMS.enums.AttemptStatus;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAttempt;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAnalyticsRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAttemptRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizStudentResultRepository;
import com.ContentManagementSystem.CMS.service.impl.QuizAttemptServiceImpl;
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
class QuizAttemptServiceImplTest {

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizStudentResultRepository quizStudentResultRepository;

    @Mock
    private QuizAnalyticsRepository quizAnalyticsRepository;

    @InjectMocks
    private QuizAttemptServiceImpl quizAttemptService;

    private QuizAttempt quizAttempt;

    @BeforeEach
    void setUp() {
        quizAttempt = new QuizAttempt();
        quizAttempt.setAttempt_id("att1");
        quizAttempt.setQuiz_id("q1");
        quizAttempt.setLearnerId("learner1");
        quizAttempt.setScore(80);
        quizAttempt.setStatus(AttemptStatus.COMPLETED);
    }

    @Test
    void createQuizAttempt_withDto_returnsAttempt() {
        QuizAttemptDto dto = new QuizAttemptDto();
        dto.setQuiz_id("q1");
        dto.setLearnerId("learner1");
        dto.setScore(80);
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(i -> i.getArgument(0));
        when(quizRepository.findById("q1")).thenReturn(null); // incrementQuizAttempts: quiz not found = no-op

        QuizAttempt result = quizAttemptService.createQuizAttempt(dto);

        assertNotNull(result);
        assertEquals("q1", result.getQuiz_id());
        verify(quizAttemptRepository).save(any(QuizAttempt.class));
    }

    @Test
    void createQuizAttempt_withModel_returnsSuccessMessage() {
        when(quizAttemptRepository.save(quizAttempt)).thenReturn(quizAttempt);
        when(quizRepository.findById("q1")).thenReturn(null); // incrementQuizAttempts: quiz not found = no-op

        String result = quizAttemptService.createQuizAttempt(quizAttempt);

        assertEquals("Quiz Attempt Created Successfully", result);
    }

    @Test
    void getQuizAttemptId_found_returnsDto() {
        when(quizAttemptRepository.findById("att1")).thenReturn(quizAttempt);

        QuizAttemptDto result = quizAttemptService.getQuizAttemptId("att1");

        assertNotNull(result);
        assertEquals("q1", result.getQuiz_id());
    }

    @Test
    void getQuizAttemptId_notFound_throwsException() {
        when(quizAttemptRepository.findById("att1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizAttemptService.getQuizAttemptId("att1"));
    }

    @Test
    void getAllQuizAttempt_returnsList() {
        when(quizAttemptRepository.findAll()).thenReturn(List.of(quizAttempt));

        List<QuizAttemptDto> result = quizAttemptService.getAllQuizAttempt();

        assertEquals(1, result.size());
    }

    @Test
    void updateQuizAttempt_found_returnsDto() {
        when(quizAttemptRepository.findById("att1")).thenReturn(quizAttempt);
        when(quizAttemptRepository.save(any())).thenReturn(quizAttempt);
        when(quizAttemptRepository.findAll()).thenReturn(List.of(quizAttempt));
        when(quizAnalyticsRepository.findAll()).thenReturn(List.of());
        when(quizAnalyticsRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(quizStudentResultRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(quizRepository.findById("q1")).thenReturn(null);

        QuizAttemptDto result = quizAttemptService.UpdateQuizAttempt("att1", quizAttempt);

        assertNotNull(result);
        assertEquals("att1", result.getAttempt_id());
        assertEquals("q1", result.getQuiz_id());
    }

    @Test
    void updateQuizAttempt_notFound_throwsException() {
        when(quizAttemptRepository.findById("att1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizAttemptService.UpdateQuizAttempt("att1", quizAttempt));
    }

    @Test
    void deleteQuizAttempt_found_returnsSuccessMessage() {
        when(quizAttemptRepository.findById("att1")).thenReturn(quizAttempt);

        String result = quizAttemptService.delete("att1");

        assertEquals("Quiz Attempt Deleted Successfully", result);
        verify(quizAttemptRepository).delete("att1");
    }

    @Test
    void deleteQuizAttempt_notFound_throwsException() {
        when(quizAttemptRepository.findById("att1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizAttemptService.delete("att1"));
    }
}
