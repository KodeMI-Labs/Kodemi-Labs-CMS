package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizOptionDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizOption;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizOptionRepository;
import com.ContentManagementSystem.CMS.service.impl.QuizOptionServiceImpl;
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
class QuizOptionServiceImplTest {

    @Mock
    private QuizOptionRepository quizOptionRepository;

    @InjectMocks
    private QuizOptionServiceImpl quizOptionService;

    private QuizOption quizOption;

    @BeforeEach
    void setUp() {
        quizOption = new QuizOption();
        quizOption.setOptionId("opt1");
        quizOption.setQuestionId("ques1");
        quizOption.setOptionText("Spring Boot");
        quizOption.setCorrect(true);
    }

    @Test
    void createQuizOption_withDto_returnsOption() {
        QuizOptionDto dto = new QuizOptionDto();
        dto.setQuestionId("ques1");
        dto.setOptionText("Spring Boot");
        dto.setCorrect(true);
        when(quizOptionRepository.save(any(QuizOption.class))).thenAnswer(i -> i.getArgument(0));

        QuizOption result = quizOptionService.createQuizOptionService(dto);

        assertNotNull(result);
        assertEquals("Spring Boot", result.getOptionText());
        verify(quizOptionRepository).save(any(QuizOption.class));
    }

    @Test
    void createQuizOption_withModel_returnsSuccessMessage() {
        when(quizOptionRepository.save(quizOption)).thenReturn(quizOption);

        String result = quizOptionService.createQuizOptionService(quizOption);

        assertEquals("Quiz Option Created Successfully", result);
    }

    @Test
    void getQuizOption_found_returnsDto() {
        when(quizOptionRepository.findById("opt1")).thenReturn(quizOption);

        QuizOptionDto result = quizOptionService.getQuizOption("opt1");

        assertNotNull(result);
        assertEquals("Spring Boot", result.getOptionText());
    }

    @Test
    void getQuizOption_notFound_throwsException() {
        when(quizOptionRepository.findById("opt1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizOptionService.getQuizOption("opt1"));
    }

    @Test
    void getAllQuizOption_returnsList() {
        when(quizOptionRepository.findAll()).thenReturn(List.of(quizOption));

        List<QuizOption> result = quizOptionService.getAllQuizOption();

        assertEquals(1, result.size());
    }

    @Test
    void updateQuizOption_found_returnsSuccessMessage() {
        when(quizOptionRepository.findById("opt1")).thenReturn(quizOption);
        when(quizOptionRepository.save(any())).thenReturn(quizOption);

        String result = quizOptionService.updateQuiz("opt1", quizOption);

        assertEquals("Quiz Option Updated Successfully", result);
    }

    @Test
    void updateQuizOption_notFound_throwsException() {
        when(quizOptionRepository.findById("opt1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizOptionService.updateQuiz("opt1", quizOption));
    }

    @Test
    void deleteQuizOption_found_returnsSuccessMessage() {
        when(quizOptionRepository.findById("opt1")).thenReturn(quizOption);

        String result = quizOptionService.delete("opt1");

        assertEquals("Quiz Option Deleted Successfully", result);
        verify(quizOptionRepository).delete("opt1");
    }

    @Test
    void deleteQuizOption_notFound_throwsException() {
        when(quizOptionRepository.findById("opt1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizOptionService.delete("opt1"));
    }
}
