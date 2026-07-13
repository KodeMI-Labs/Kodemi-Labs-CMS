package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizAnswerDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAnswer;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAnswerRepository;
import com.ContentManagementSystem.CMS.service.impl.QuizAnswerServiceImpl;
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
class QuizAnswerServiceImplTest {

    @Mock
    private QuizAnswerRepository quizAnswerRepository;

    @InjectMocks
    private QuizAnswerServiceImpl quizAnswerService;

    private QuizAnswer quizAnswer;

    @BeforeEach
    void setUp() {
        quizAnswer = new QuizAnswer();
        quizAnswer.setAnswer_id("a1");
        quizAnswer.setAttempt_id("att1");
        quizAnswer.setQuestion_id("ques1");
        quizAnswer.setSelectedOption_id("opt1");
        quizAnswer.setCorrect(true);
    }

    @Test
    void createQuizAnswer_withDto_returnsAnswer() {
        QuizAnswerDto dto = new QuizAnswerDto();
        dto.setAttempt_id("att1");
        dto.setQuestion_id("ques1");
        dto.setSelectedOption_id("opt1");
        dto.setCorrect(true);
        when(quizAnswerRepository.save(any(QuizAnswer.class))).thenAnswer(i -> i.getArgument(0));

        QuizAnswer result = quizAnswerService.createQuizAnswer(dto);

        assertNotNull(result);
        assertEquals("att1", result.getAttempt_id());
        verify(quizAnswerRepository).save(any(QuizAnswer.class));
    }

    @Test
    void createQuizAnswer_withModel_returnsSuccessMessage() {
        when(quizAnswerRepository.save(quizAnswer)).thenReturn(quizAnswer);

        String result = quizAnswerService.createQuizAnswer(quizAnswer);

        assertEquals("Quiz Answer Created Successfully", result);
    }

    @Test
    void getQuizAnswerId_found_returnsDto() {
        when(quizAnswerRepository.findById("a1")).thenReturn(quizAnswer);

        QuizAnswerDto result = quizAnswerService.getQuizAnswerId("a1");

        assertNotNull(result);
        assertEquals("att1", result.getAttempt_id());
    }

    @Test
    void getQuizAnswerId_notFound_throwsException() {
        when(quizAnswerRepository.findById("a1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizAnswerService.getQuizAnswerId("a1"));
    }

    @Test
    void getAllQuizAnswer_returnsList() {
        when(quizAnswerRepository.findAll()).thenReturn(List.of(quizAnswer));

        List<QuizAnswerDto> result = quizAnswerService.getAllQuizAnswer();

        assertEquals(1, result.size());
    }

    @Test
    void updateQuizAnswer_found_returnsSuccessMessage() {
        when(quizAnswerRepository.findById("a1")).thenReturn(quizAnswer);
        when(quizAnswerRepository.save(any())).thenReturn(quizAnswer);

        String result = quizAnswerService.UpdateQuizAnswer("a1", quizAnswer);

        assertEquals("Quiz Answer Updated Successfully", result);
    }

    @Test
    void updateQuizAnswer_notFound_throwsException() {
        when(quizAnswerRepository.findById("a1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizAnswerService.UpdateQuizAnswer("a1", quizAnswer));
    }

    @Test
    void deleteQuizAnswer_found_returnsSuccessMessage() {
        when(quizAnswerRepository.findById("a1")).thenReturn(quizAnswer);

        String result = quizAnswerService.delete("a1");

        assertEquals("Quiz Answer Deleted Successfully", result);
        verify(quizAnswerRepository).delete("a1");
    }

    @Test
    void deleteQuizAnswer_notFound_throwsException() {
        when(quizAnswerRepository.findById("a1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizAnswerService.delete("a1"));
    }
}
