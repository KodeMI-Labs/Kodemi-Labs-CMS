package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.client.CourseServiceClient;
import com.ContentManagementSystem.CMS.dto.reviewsandratings.request.ReviewRequest;
import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.ReviewResponse;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.reviewsandratings.CourseReview;
import com.ContentManagementSystem.CMS.repository.reviewsandratingrepository.CourseRatingSummaryRepository;
import com.ContentManagementSystem.CMS.repository.reviewsandratingrepository.CourseReviewRepository;
import com.ContentManagementSystem.CMS.service.impl.CourseReviewServiceImpl;
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
class CourseReviewServiceImplTest {

    @Mock
    private CourseReviewRepository courseReviewRepository;

    @Mock
    private CourseRatingSummaryRepository courseRatingSummaryRepository;

    @Mock
    private CourseServiceClient courseServiceClient;

    @InjectMocks
    private CourseReviewServiceImpl courseReviewService;

    private CourseReview courseReview;

    @BeforeEach
    void setUp() {
        courseReview = new CourseReview();
        courseReview.setReview_id("r1");
        courseReview.setCourse_id("course1");
        courseReview.setLearner_id("learner1");
        courseReview.setLearner_name("John");
        courseReview.setRating(5);
        courseReview.setReview_comment("Excellent course!");
        courseReview.setEdited(Boolean.FALSE);
        courseReview.setApproved(Boolean.TRUE);
    }

    @Test
    void createCourseReview_withRequest_returnsCourseReview() {
        ReviewRequest request = new ReviewRequest();
        request.setCourse_id("course1");
        request.setRating(5);
        request.setComment("Excellent course!");
        when(courseReviewRepository.save(any(CourseReview.class))).thenAnswer(i -> i.getArgument(0));

        CourseReview result = courseReviewService.createCourseReview(request);

        assertNotNull(result);
        assertNotNull(result.getReview_id());
        assertEquals("course1", result.getCourse_id());
        assertEquals(5, result.getRating());
        verify(courseReviewRepository).save(any(CourseReview.class));
    }

    @Test
    void createCourseReview_withModel_returnsSuccessMessage() {
        when(courseReviewRepository.save(any(CourseReview.class))).thenReturn(courseReview);

        String result = courseReviewService.createCourseReview(courseReview);

        assertEquals("Course Review Created Successfully", result);
    }

    @Test
    void getCourseReviewById_found_returnsResponse() {
        when(courseReviewRepository.findById("r1")).thenReturn(courseReview);

        ReviewResponse result = courseReviewService.getCourseReviewById("r1");

        assertNotNull(result);
        assertEquals("John", result.getLearnerName());
        assertEquals(5, result.getRating());
    }

    @Test
    void getCourseReviewById_notFound_throwsException() {
        when(courseReviewRepository.findById("r1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseReviewService.getCourseReviewById("r1"));
    }

    @Test
    void getAllCourseReviews_returnsList() {
        when(courseReviewRepository.findAll()).thenReturn(List.of(courseReview));

        List<ReviewResponse> result = courseReviewService.getAllCourseReviews();

        assertEquals(1, result.size());
    }

    @Test
    void getReviewsByCourse_returnsFilteredList() {
        // getReviewsByCourse now uses GSI query via findByCourseId
        when(courseReviewRepository.findByCourseId("course1")).thenReturn(List.of(courseReview));

        List<ReviewResponse> result = courseReviewService.getReviewsByCourse("course1");

        assertEquals(1, result.size());
    }

    @Test
    void updateCourseReview_found_returnsSuccessMessage() {
        when(courseReviewRepository.findById("r1")).thenReturn(courseReview);
        when(courseReviewRepository.save(any())).thenReturn(courseReview);

        String result = courseReviewService.updateCourseReview("r1", courseReview);

        assertEquals("Course Review Updated Successfully", result);
    }

    @Test
    void updateCourseReview_notFound_throwsException() {
        when(courseReviewRepository.findById("r1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseReviewService.updateCourseReview("r1", courseReview));
    }

    @Test
    void deleteCourseReview_found_returnsSuccessMessage() {
        when(courseReviewRepository.findById("r1")).thenReturn(courseReview);

        String result = courseReviewService.delete("r1");

        assertEquals("Course Review Deleted Successfully", result);
        verify(courseReviewRepository).delete("r1");
    }

    @Test
    void deleteCourseReview_notFound_throwsException() {
        when(courseReviewRepository.findById("r1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseReviewService.delete("r1"));
    }
}
