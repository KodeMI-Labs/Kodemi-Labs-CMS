package com.ContentManagementSystem.CMS.controller.quizcontroller;
import com.ContentManagementSystem.CMS.dto.quizDto.QuizAnalyticsDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAnalytics;
import com.ContentManagementSystem.CMS.service.QuizAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
