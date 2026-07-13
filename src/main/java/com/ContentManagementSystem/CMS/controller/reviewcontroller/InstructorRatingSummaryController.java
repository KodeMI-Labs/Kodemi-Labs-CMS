package com.ContentManagementSystem.CMS.controller.reviewcontroller;

import com.ContentManagementSystem.CMS.dto.reviewsandratings.response.RatingSummaryResponse;
import com.ContentManagementSystem.CMS.model.reviewsandratings.InstructorRatingSummary;
import com.ContentManagementSystem.CMS.service.InstructorRatingSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
