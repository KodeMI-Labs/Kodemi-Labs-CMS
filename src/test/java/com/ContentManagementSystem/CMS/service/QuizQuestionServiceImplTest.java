package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizQuestionDto;
import com.ContentManagementSystem.CMS.enums.DifficultyLevel;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizQuestion;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizOptionRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizQuestionRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizRepository;
import com.ContentManagementSystem.CMS.service.impl.QuizQuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class QuizQuestionServiceImplTest {

    @Mock
    private QuizQuestionRepository quizQuestionRepository;

    @Mock
    private QuizOptionRepository quizOptionRepository;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizQuestionServiceImpl quizQuestionService;

    private QuizQuestion quizQuestion;

    @BeforeEach
    void setUp() {
        quizQuestion = new QuizQuestion();
        quizQuestion.setQuestionId("ques1");
        quizQuestion.setQuiz_id("q1");
        quizQuestion.setQuestion("What is Spring Boot?");
        quizQuestion.setDifficultyLevel(DifficultyLevel.EASY);
        quizQuestion.setMarks(5);
        quizQuestion.setSequenceNo(1);
    }

    @Test
    void createQuizQuestion_withDto_returnsQuestion() {
        QuizQuestionDto dto = new QuizQuestionDto();
        dto.setQuiz_id("q1");
        dto.setQuestion("What is Spring Boot?");
        dto.setDifficultyLevel(DifficultyLevel.EASY);
        dto.setMarks(5);
        when(quizQuestionRepository.save(any(QuizQuestion.class))).thenAnswer(i -> i.getArgument(0));
        lenient().when(quizRepository.findById(anyString())).thenReturn(null);

        QuizQuestion result = quizQuestionService.createQuizQuestion(dto);

        assertNotNull(result);
        assertEquals("What is Spring Boot?", result.getQuestion());
        verify(quizQuestionRepository).save(any(QuizQuestion.class));
    }

    @Test
    void createQuizQuestion_withModel_returnsSuccessMessage() {
        when(quizQuestionRepository.save(quizQuestion)).thenReturn(quizQuestion);
        lenient().when(quizRepository.findById(anyString())).thenReturn(null);

        String result = quizQuestionService.createQuizQuestion(quizQuestion);

        assertEquals("Quiz Question Created Successfully", result);
    }

    @Test
    void getQuizQuestion_found_returnsDto() {
        when(quizQuestionRepository.findById("ques1")).thenReturn(quizQuestion);

        QuizQuestionDto result = quizQuestionService.getQuizQuestion("ques1");

        assertNotNull(result);
        assertEquals("What is Spring Boot?", result.getQuestion());
    }

    @Test
    void getQuizQuestion_notFound_throwsException() {
        when(quizQuestionRepository.findById("ques1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizQuestionService.getQuizQuestion("ques1"));
    }

    @Test
    void getAllQuizQuestion_returnsList() {
        when(quizQuestionRepository.findAll()).thenReturn(List.of(quizQuestion));

        List<QuizQuestionDto> result = quizQuestionService.getAllQuizQuestion();

        assertEquals(1, result.size());
    }

    @Test
    void updateQuizQuestion_found_returnsSuccessMessage() {
        when(quizQuestionRepository.findById("ques1")).thenReturn(quizQuestion);
        when(quizQuestionRepository.save(any())).thenReturn(quizQuestion);

        String result = quizQuestionService.updateQuiz("ques1", quizQuestion);

        assertEquals("Quiz Question Updated Successfully", result);
    }

    @Test
    void updateQuizQuestion_notFound_throwsException() {
        when(quizQuestionRepository.findById("ques1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizQuestionService.updateQuiz("ques1", quizQuestion));
    }

    @Test
    void deleteQuizQuestion_found_returnsSuccessMessage() {
        when(quizQuestionRepository.findById("ques1")).thenReturn(quizQuestion);
        when(quizOptionRepository.findAll()).thenReturn(List.of());

        String result = quizQuestionService.delete("ques1");

        assertEquals("Quiz Question and its Options Deleted Successfully", result);
        verify(quizQuestionRepository).delete("ques1");
    }

    @Test
    void deleteQuizQuestion_notFound_throwsException() {
        when(quizQuestionRepository.findById("ques1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizQuestionService.delete("ques1"));
    }
}
