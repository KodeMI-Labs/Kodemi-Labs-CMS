package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizCategoryDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizCategory;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizCategoryRepository;
import com.ContentManagementSystem.CMS.service.impl.QuizCategoryServiceImpl;
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
class QuizCategoryServiceImplTest {

    @Mock
    private QuizCategoryRepository quizCategoryRepository;

    @InjectMocks
    private QuizCategoryServiceImpl quizCategoryService;

    private QuizCategory quizCategory;

    @BeforeEach
    void setUp() {
        quizCategory = new QuizCategory();
        quizCategory.setCategoryId("c1");
        quizCategory.setCategoryName("Java");
        quizCategory.setDescription("Java category");
        quizCategory.setActive(true);
    }

    @Test
    void createQuizCategory_withDto_returnsCategory() {
        QuizCategoryDto dto = new QuizCategoryDto();
        dto.setCategoryName("Java");
        dto.setDescription("Java category");
        dto.setActive(true);
        when(quizCategoryRepository.save(any(QuizCategory.class))).thenAnswer(i -> i.getArgument(0));

        QuizCategory result = quizCategoryService.createQuizCategory(dto);

        assertNotNull(result);
        assertEquals("Java", result.getCategoryName());
        verify(quizCategoryRepository).save(any(QuizCategory.class));
    }

    @Test
    void createQuizCategory_withModel_returnsSuccessMessage() {
        when(quizCategoryRepository.save(quizCategory)).thenReturn(quizCategory);

        String result = quizCategoryService.createQuizCategory(quizCategory);

        assertEquals("Quiz Category Created Successfully", result);
    }

    @Test
    void getQuizCategory_found_returnsDto() {
        when(quizCategoryRepository.findById("c1")).thenReturn(quizCategory);

        QuizCategoryDto result = quizCategoryService.getQuizCategory("c1");

        assertNotNull(result);
        assertEquals("Java", result.getCategoryName());
    }

    @Test
    void getQuizCategory_notFound_throwsException() {
        when(quizCategoryRepository.findById("c1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizCategoryService.getQuizCategory("c1"));
    }

    @Test
    void getAllQuizCategory_returnsList() {
        when(quizCategoryRepository.findAll()).thenReturn(List.of(quizCategory));

        List<QuizCategoryDto> result = quizCategoryService.getAllQuizCategory();

        assertEquals(1, result.size());
    }

    @Test
    void updateQuizCategory_found_returnsSuccessMessage() {
        when(quizCategoryRepository.findById("c1")).thenReturn(quizCategory);
        when(quizCategoryRepository.save(any())).thenReturn(quizCategory);

        String result = quizCategoryService.UpdateQuizCategory("c1", quizCategory);

        assertEquals("Quiz Category Updated Successfully", result);
    }

    @Test
    void updateQuizCategory_notFound_throwsException() {
        when(quizCategoryRepository.findById("c1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizCategoryService.UpdateQuizCategory("c1", quizCategory));
    }

    @Test
    void deleteQuizCategory_found_returnsSuccessMessage() {
        when(quizCategoryRepository.findById("c1")).thenReturn(quizCategory);

        String result = quizCategoryService.delete("c1");

        assertEquals("Quiz Category Deleted Successfully", result);
        verify(quizCategoryRepository).delete("c1");
    }

    @Test
    void deleteQuizCategory_notFound_throwsException() {
        when(quizCategoryRepository.findById("c1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> quizCategoryService.delete("c1"));
    }
}
