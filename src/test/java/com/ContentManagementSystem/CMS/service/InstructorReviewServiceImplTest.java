package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.request.InstructorReviewRequest;
import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.ReviewResponse;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorReview;
import com.ContentManagementSystem.CMS.repository.reviewsandratingrepository.InstructorReviewRepository;
import com.ContentManagementSystem.CMS.service.impl.InstructorReviewServiceImpl;
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
class InstructorReviewServiceImplTest {

    @Mock
    private InstructorReviewRepository instructorReviewRepository;

    @InjectMocks
    private InstructorReviewServiceImpl instructorReviewService;

    private InstructorReview instructorReview;

    @BeforeEach
    void setUp() {
        instructorReview = new InstructorReview();
        instructorReview.setReview_id("r1");
        instructorReview.setInstructor("instructor1");   // field is named 'instructor' in model
        instructorReview.setLearner_id("learner1");
        instructorReview.setLearner_name("Jane");
        instructorReview.setRating(4);
        instructorReview.setComment("Great instructor!");
    }

    @Test
    void createInstructorReview_withRequest_returnsReview() {
        InstructorReviewRequest request = new InstructorReviewRequest();
        request.setInstructor_id("instructor1");
        request.setRating(4);
        request.setComment("Great instructor!");
        when(instructorReviewRepository.save(any(InstructorReview.class))).thenAnswer(i -> i.getArgument(0));

        InstructorReview result = instructorReviewService.createInstructorReview(request);

        assertNotNull(result);
        assertNotNull(result.getReview_id());
        assertEquals("instructor1", result.getInstructor()); // field is 'instructor'
        assertEquals(4, result.getRating());
        verify(instructorReviewRepository).save(any(InstructorReview.class));
    }

    @Test
    void createInstructorReview_withModel_returnsSuccessMessage() {
        when(instructorReviewRepository.save(any(InstructorReview.class))).thenReturn(instructorReview);

        String result = instructorReviewService.createInstructorReview(instructorReview);

        assertEquals("Instructor Review Created Successfully", result);
    }

    @Test
    void getInstructorReviewById_found_returnsResponse() {
        when(instructorReviewRepository.findById("r1")).thenReturn(instructorReview);

        ReviewResponse result = instructorReviewService.getInstructorReviewById("r1");

        assertNotNull(result);
        assertEquals("Jane", result.getLearnerName());
        assertEquals(4, result.getRating());
    }

    @Test
    void getInstructorReviewById_notFound_throwsException() {
        when(instructorReviewRepository.findById("r1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> instructorReviewService.getInstructorReviewById("r1"));
    }

    @Test
    void getAllInstructorReviews_returnsList() {
        when(instructorReviewRepository.findAll()).thenReturn(List.of(instructorReview));

        List<ReviewResponse> result = instructorReviewService.getAllInstructorReviews();

        assertEquals(1, result.size());
    }

    @Test
    void getReviewsByInstructor_returnsFilteredList() {
        InstructorReview other = new InstructorReview();
        other.setReview_id("r2");
        other.setInstructor("other-instructor"); // field is 'instructor'
        when(instructorReviewRepository.findAll()).thenReturn(List.of(instructorReview, other));

        List<ReviewResponse> result = instructorReviewService.getReviewsByInstructor("instructor1");

        assertEquals(1, result.size());
    }

    @Test
    void updateInstructorReview_found_returnsSuccessMessage() {
        when(instructorReviewRepository.findById("r1")).thenReturn(instructorReview);
        when(instructorReviewRepository.save(any())).thenReturn(instructorReview);

        String result = instructorReviewService.updateInstructorReview("r1", instructorReview);

        assertEquals("Instructor Review Updated Successfully", result);
    }

    @Test
    void updateInstructorReview_notFound_throwsException() {
        when(instructorReviewRepository.findById("r1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> instructorReviewService.updateInstructorReview("r1", instructorReview));
    }

    @Test
    void deleteInstructorReview_found_returnsSuccessMessage() {
        when(instructorReviewRepository.findById("r1")).thenReturn(instructorReview);

        String result = instructorReviewService.delete("r1");

        assertEquals("Instructor Review Deleted Successfully", result);
        verify(instructorReviewRepository).delete("r1");
    }

    @Test
    void deleteInstructorReview_notFound_throwsException() {
        when(instructorReviewRepository.findById("r1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> instructorReviewService.delete("r1"));
    }
}
