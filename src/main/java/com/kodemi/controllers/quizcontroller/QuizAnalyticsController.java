package com.kodemi.controllers.quizcontroller;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.reviewsandratings.quizDto.QuizAnalyticsDto;
import com.kodemi.model.quizmodel.QuizAnalytics;
import com.kodemi.service.QuizAnalyticsService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/quiz-analytics")
@RequiredArgsConstructor
public class QuizAnalyticsController {

    private final QuizAnalyticsService quizAnalyticsService;

    @PostMapping("/create")
    public String createQuizAnalytics(@RequestBody QuizAnalytics quizAnalytics) {
        return quizAnalyticsService.createQuizAnalytics(quizAnalytics);
    }
    @PostMapping("/create-dto")
    public QuizAnalytics createQuizAnalyticsDto(@RequestBody QuizAnalyticsDto dto) {
        return quizAnalyticsService.createQuizAnalytics(dto);
    }
    @GetMapping("/{analytics_id}")
    public QuizAnalyticsDto getQuizAnalyticsById(@PathVariable String analytics_id) {
        return quizAnalyticsService.getQuizAnalyticsById(analytics_id);
    }

    @GetMapping("/quiz/{quiz_id}")
    public QuizAnalyticsDto getQuizAnalyticsByQuizId(@PathVariable String quiz_id) {
        return quizAnalyticsService.getQuizAnalyticsByQuizId(quiz_id);
    }
    @GetMapping("/all")
    public List<QuizAnalyticsDto> getAllQuizAnalytics() {
        return quizAnalyticsService.getAllQuizAnalytics();
    }
    @PutMapping("/update/{analytics_id}")
    public String updateQuizAnalytics(@PathVariable String analytics_id,
                                      @RequestBody QuizAnalytics quizAnalytics) {
        return quizAnalyticsService.updateQuizAnalytics(analytics_id, quizAnalytics);
    }
    @DeleteMapping("/delete/{analytics_id}")
    public String deleteQuizAnalytics(@PathVariable String analytics_id) {
        return quizAnalyticsService.delete(analytics_id);
    }
    @PostMapping("/recalculate/{quiz_id}")
    public String recalculateAnalytics(@PathVariable String quiz_id) {
        return quizAnalyticsService.recalculateAnalytics(quiz_id);
    }
    @PostMapping("/recalculate-all")
    public String recalculateAllAnalytics() {
        return quizAnalyticsService.recalculateAllAnalytics();
    }
}
