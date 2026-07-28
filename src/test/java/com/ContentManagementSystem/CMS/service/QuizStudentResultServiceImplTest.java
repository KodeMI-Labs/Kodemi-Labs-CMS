package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizStudentResultDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizStudentResult;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizStudentResultRepository;
import com.ContentManagementSystem.CMS.service.impl.QuizStudentResultServiceImpl;
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
class QuizStudentResultServiceImplTest {

    @Mock
    private QuizStudentResultRepository quizStudentResultRepository;

    @InjectMocks
    private QuizStudentResultServiceImpl quizStudentResultService;

    private QuizStudentResult result;

    @BeforeEach
    void setUp() {
        result = new QuizStudentResult();
        result.setResult_id("r1");
        result.setQuiz_id("q1");
        result.setLearner_id("learner1");
        result.setLearner_name("Leena");
        result.setQuestions_attempted(22);
        result.setTotal_questions(25);
        result.setScore(18);
        result.setTotal_marks(25);
        result.setPercent(72.0);
        result.setTime_taken_minutes(18);
        result.setStatus("Submitted");
    }

    @Test
    void createResult_withDto_returnsResult() {
        QuizStudentResultDto dto = new QuizStudentResultDto();
        dto.setQuiz_id("q1");
        dto.setLearner_id("learner1");
        dto.setLearner_name("Leena");
        dto.setScore(18);
        dto.setPercent(72.0);
        dto.setStatus("Submitted");
        when(quizStudentResultRepository.save(any(QuizStudentResult.class))).thenAnswer(i -> i.getArgument(0));

        QuizStudentResult created = quizStudentResultService.createResult(dto);

        assertNotNull(created);
        assertNotNull(created.getResult_id());
        assertEquals("Leena", created.getLearner_name());
        verify(quizStudentResultRepository).save(any(QuizStudentResult.class));
    }

    @Test
    void createResult_withModel_returnsSuccessMessage() {
        when(quizStudentResultRepository.save(any(QuizStudentResult.class))).thenReturn(result);

        String msg = quizStudentResultService.createResult(result);

        assertEquals("Quiz Student Result Created Successfully", msg);
    }

    @Test
    void getResultById_found_returnsDto() {
        when(quizStudentResultRepository.findById("r1")).thenReturn(result);

        QuizStudentResultDto dto = quizStudentResultService.getResultById("r1");

        assertNotNull(dto);
        assertEquals("Leena", dto.getLearner_name());
        assertEquals(72.0, dto.getPercent());
    }

    @Test
    void getResultById_notFound_throwsException() {
        when(quizStudentResultRepository.findById("r1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> quizStudentResultService.getResultById("r1"));
    }

    @Test
    void getAllResults_returnsList() {
        when(quizStudentResultRepository.findAll()).thenReturn(List.of(result));

        List<QuizStudentResultDto> list = quizStudentResultService.getAllResults();

        assertEquals(1, list.size());
    }

    @Test
    void getResultsByQuizId_returnsFilteredList() {
        QuizStudentResult other = new QuizStudentResult();
        other.setResult_id("r2");
        other.setQuiz_id("other-quiz");
        when(quizStudentResultRepository.findAll()).thenReturn(List.of(result, other));

        List<QuizStudentResultDto> list = quizStudentResultService.getResultsByQuizId("q1");

        assertEquals(1, list.size());
        assertEquals("q1", list.get(0).getQuiz_id());
    }

    @Test
    void getResultsByLearnerId_returnsFilteredList() {
        QuizStudentResult other = new QuizStudentResult();
        other.setResult_id("r2");
        other.setLearner_id("other-learner");
        when(quizStudentResultRepository.findAll()).thenReturn(List.of(result, other));

        List<QuizStudentResultDto> list = quizStudentResultService.getResultsByLearnerId("learner1");

        assertEquals(1, list.size());
        assertEquals("learner1", list.get(0).getLearner_id());
    }

    @Test
    void updateResult_found_returnsSuccessMessage() {
        when(quizStudentResultRepository.findById("r1")).thenReturn(result);
        when(quizStudentResultRepository.save(any())).thenReturn(result);

        String msg = quizStudentResultService.updateResult("r1", result);

        assertEquals("Quiz Student Result Updated Successfully", msg);
    }

    @Test
    void updateResult_notFound_throwsException() {
        when(quizStudentResultRepository.findById("r1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> quizStudentResultService.updateResult("r1", result));
    }

    @Test
    void deleteResult_found_returnsSuccessMessage() {
        when(quizStudentResultRepository.findById("r1")).thenReturn(result);

        String msg = quizStudentResultService.delete("r1");

        assertEquals("Quiz Student Result Deleted Successfully", msg);
        verify(quizStudentResultRepository).delete("r1");
    }

    @Test
    void deleteResult_notFound_throwsException() {
        when(quizStudentResultRepository.findById("r1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> quizStudentResultService.delete("r1"));
    }
}
