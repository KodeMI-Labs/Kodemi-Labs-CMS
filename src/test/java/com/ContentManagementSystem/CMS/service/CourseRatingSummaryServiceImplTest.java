package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.RatingSummaryResponse;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.reviewsandratings.CourseRatingSummary;
import com.ContentManagementSystem.CMS.repository.reviewsandratingrepository.CourseRatingSummaryRepository;
import com.ContentManagementSystem.CMS.service.impl.CourseRatingSummaryServiceImpl;
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
class CourseRatingSummaryServiceImplTest {

    @Mock
    private CourseRatingSummaryRepository courseRatingSummaryRepository;

    @InjectMocks
    private CourseRatingSummaryServiceImpl courseRatingSummaryService;

    private CourseRatingSummary summary;

    @BeforeEach
    void setUp() {
        summary = new CourseRatingSummary();
        summary.setCourse_id("course1");
        summary.setAverage_rating(4.5);
        summary.setTotal_reviews(100);
        summary.setFive_star_count(60);
        summary.setFour_star_count(25);
        summary.setThree_star_count(10);
        summary.setTwo_star_count(3);
        summary.setOne_star_count(2);
    }

    @Test
    void createCourseRatingSummary_withResponse_returnsSummary() {
        RatingSummaryResponse response = new RatingSummaryResponse();
        response.setAverage_rating(4.5);
        response.setTotal_reviews(100);
        when(courseRatingSummaryRepository.save(any(CourseRatingSummary.class))).thenAnswer(i -> i.getArgument(0));

        CourseRatingSummary result = courseRatingSummaryService.createCourseRatingSummary("course1", response);

        assertNotNull(result);
        verify(courseRatingSummaryRepository).save(any(CourseRatingSummary.class));
    }

    @Test
    void createCourseRatingSummary_withModel_returnsSuccessMessage() {
        when(courseRatingSummaryRepository.save(summary)).thenReturn(summary);

        String result = courseRatingSummaryService.createCourseRatingSummary(summary);

        assertEquals("Course Rating Summary Created Successfully", result);
    }

    @Test
    void getRatingSummaryId_found_returnsResponse() {
        when(courseRatingSummaryRepository.findById("course1")).thenReturn(summary);

        RatingSummaryResponse result = courseRatingSummaryService.getRatingSummaryId("course1");

        assertNotNull(result);
        assertEquals(4.5, result.getAverage_rating());
        assertEquals(100, result.getTotal_reviews());
    }

    @Test
    void getRatingSummaryId_notFound_throwsException() {
        when(courseRatingSummaryRepository.findById("course1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseRatingSummaryService.getRatingSummaryId("course1"));
    }

    @Test
    void getAllCourseRatingSummary_returnsList() {
        when(courseRatingSummaryRepository.findAll()).thenReturn(List.of(summary));

        List<RatingSummaryResponse> result = courseRatingSummaryService.getAllCourseRatingSummary();

        assertEquals(1, result.size());
    }

    @Test
    void updateCourseRatingSummary_found_returnsSuccessMessage() {
        when(courseRatingSummaryRepository.findById("course1")).thenReturn(summary);
        when(courseRatingSummaryRepository.save(any())).thenReturn(summary);

        String result = courseRatingSummaryService.updateCourseRatingSummary("course1", summary);

        assertEquals("Course Rating Summary Updated Successfully", result);
    }

    @Test
    void updateCourseRatingSummary_notFound_throwsException() {
        when(courseRatingSummaryRepository.findById("course1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseRatingSummaryService.updateCourseRatingSummary("course1", summary));
    }

    @Test
    void deleteCourseRatingSummary_found_returnsSuccessMessage() {
        when(courseRatingSummaryRepository.findById("course1")).thenReturn(summary);

        String result = courseRatingSummaryService.delete("course1");

        assertEquals("Course Rating Summary Deleted Successfully", result);
        verify(courseRatingSummaryRepository).delete("course1");
    }

    @Test
    void deleteCourseRatingSummary_notFound_throwsException() {
        when(courseRatingSummaryRepository.findById("course1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseRatingSummaryService.delete("course1"));
    }
}
