package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.RatingSummaryResponse;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorRatingSummary;
import com.ContentManagementSystem.CMS.repository.reviewsandratingrepository.InstructorRatingSummaryRepository;
import com.ContentManagementSystem.CMS.service.impl.InstructorRatingSummaryServiceImpl;
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
class InstructorRatingSummaryServiceImplTest {

    @Mock
    private InstructorRatingSummaryRepository instructorRatingSummaryRepository;

    @InjectMocks
    private InstructorRatingSummaryServiceImpl instructorRatingSummaryService;

    private InstructorRatingSummary summary;

    @BeforeEach
    void setUp() {
        summary = new InstructorRatingSummary();
        summary.setInstructor_id("instructor1");
        summary.setAverage_rating(4.8);
        summary.setTotal_reviews(50);
        summary.setFive_star_count(40);
        summary.setFour_star_count(7);
        summary.setThree_star_count(2);
        summary.setTwoStarCount(1);
        summary.setOne_star_count(0);
    }

    @Test
    void createInstructorRatingSummary_withResponse_returnsSummary() {
        RatingSummaryResponse response = new RatingSummaryResponse();
        response.setAverage_rating(4.8);
        response.setTotal_reviews(50);
        when(instructorRatingSummaryRepository.save(any(InstructorRatingSummary.class)))
                .thenAnswer(i -> i.getArgument(0));

        InstructorRatingSummary result = instructorRatingSummaryService.createInstructorRatingSummary("instructor1", response);

        assertNotNull(result);
        verify(instructorRatingSummaryRepository).save(any(InstructorRatingSummary.class));
    }

    @Test
    void createInstructorRatingSummary_withModel_returnsSuccessMessage() {
        when(instructorRatingSummaryRepository.save(summary)).thenReturn(summary);

        String result = instructorRatingSummaryService.createInstructorRatingSummary(summary);

        assertEquals("Instructor Rating Summary Created Successfully", result);
    }

    @Test
    void getInstructorRatingSummaryById_found_returnsResponse() {
        when(instructorRatingSummaryRepository.findById("instructor1")).thenReturn(summary);

        RatingSummaryResponse result = instructorRatingSummaryService.getInstructorRatingSummaryById("instructor1");

        assertNotNull(result);
        assertEquals(4.8, result.getAverage_rating());
        assertEquals(50, result.getTotal_reviews());
    }

    @Test
    void getInstructorRatingSummaryById_notFound_throwsException() {
        when(instructorRatingSummaryRepository.findById("instructor1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> instructorRatingSummaryService.getInstructorRatingSummaryById("instructor1"));
    }

    @Test
    void getAllInstructorRatingSummaries_returnsList() {
        when(instructorRatingSummaryRepository.findAll()).thenReturn(List.of(summary));

        List<RatingSummaryResponse> result = instructorRatingSummaryService.getAllInstructorRatingSummaries();

        assertEquals(1, result.size());
    }

    @Test
    void updateInstructorRatingSummary_found_returnsSuccessMessage() {
        when(instructorRatingSummaryRepository.findById("instructor1")).thenReturn(summary);
        when(instructorRatingSummaryRepository.save(any())).thenReturn(summary);

        String result = instructorRatingSummaryService.updateInstructorRatingSummary("instructor1", summary);

        assertEquals("Instructor Rating Summary Updated Successfully", result);
    }

    @Test
    void updateInstructorRatingSummary_notFound_throwsException() {
        when(instructorRatingSummaryRepository.findById("instructor1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> instructorRatingSummaryService.updateInstructorRatingSummary("instructor1", summary));
    }

    @Test
    void deleteInstructorRatingSummary_found_returnsSuccessMessage() {
        when(instructorRatingSummaryRepository.findById("instructor1")).thenReturn(summary);

        String result = instructorRatingSummaryService.delete("instructor1");

        assertEquals("Instructor Rating Summary Deleted Successfully", result);
        verify(instructorRatingSummaryRepository).delete("instructor1");
    }

    @Test
    void deleteInstructorRatingSummary_notFound_throwsException() {
        when(instructorRatingSummaryRepository.findById("instructor1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> instructorRatingSummaryService.delete("instructor1"));
    }
}
