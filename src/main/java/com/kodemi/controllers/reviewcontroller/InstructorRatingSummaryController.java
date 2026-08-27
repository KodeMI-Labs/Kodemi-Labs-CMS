package com.kodemi.controllers.reviewcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.reviewsandratings.response.RatingSummaryResponse;
import com.kodemi.model.reviewsandratings.InstructorRatingSummary;
import com.kodemi.service.InstructorRatingSummaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/instructor-rating-summary")
@RequiredArgsConstructor
public class InstructorRatingSummaryController {

    private final InstructorRatingSummaryService instructorRatingSummaryService;

    @PostMapping("/create")
    public String createInstructorRatingSummary(@RequestBody InstructorRatingSummary instructorRatingSummary) {
        return instructorRatingSummaryService.createInstructorRatingSummary(instructorRatingSummary);
    }

    @PostMapping("/create-dto/{instructor_id}")
    public InstructorRatingSummary createInstructorRatingSummaryDto(
            @PathVariable String instructor_id,
            @RequestBody RatingSummaryResponse ratingSummaryResponse) {
        return instructorRatingSummaryService.createInstructorRatingSummary(instructor_id, ratingSummaryResponse);
    }

    @GetMapping("/{instructor_id}")
    public RatingSummaryResponse getInstructorRatingSummaryById(@PathVariable String instructor_id) {
        return instructorRatingSummaryService.getInstructorRatingSummaryById(instructor_id);
    }

    @GetMapping("/all")
    public List<RatingSummaryResponse> getAllInstructorRatingSummaries() {
        return instructorRatingSummaryService.getAllInstructorRatingSummaries();
    }

    @PutMapping("/update/{instructor_id}")
    public String updateInstructorRatingSummary(@PathVariable String instructor_id, @RequestBody InstructorRatingSummary instructorRatingSummary) {
        return instructorRatingSummaryService.updateInstructorRatingSummary(instructor_id, instructorRatingSummary);
    }

    @DeleteMapping("/delete/{instructor_id}")
    public String deleteInstructorRatingSummary(@PathVariable String instructor_id) {
        return instructorRatingSummaryService.delete(instructor_id);
    }
}
