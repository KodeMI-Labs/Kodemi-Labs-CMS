package com.ContentManagementSystem.CMS.controller.quizcontroller;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizAttemptDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAttempt;
import com.ContentManagementSystem.CMS.service.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz-attempt")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    @PostMapping("/create")
    public String createQuizAttempt(@RequestBody QuizAttempt quizAttempt) {
        return quizAttemptService.createQuizAttempt(quizAttempt);
    }

    @PostMapping("/create-dto")
    public QuizAttempt createQuizAttemptDto(@RequestBody QuizAttemptDto quizAttemptDto) {
        return quizAttemptService.createQuizAttempt(quizAttemptDto);
    }

    @GetMapping("/{attempt_id}")
    public QuizAttemptDto getQuizAttemptById(@PathVariable String attempt_id) {
        return quizAttemptService.getQuizAttemptId(attempt_id);
    }

    @GetMapping("/all")
    public List<QuizAttemptDto> getAllQuizAttempt() {
        return quizAttemptService.getAllQuizAttempt();
    }

    @GetMapping("/learner/{learnerId}")
    public List<QuizAttemptDto> getAttemptsByLearner(@PathVariable String learnerId) {
        return quizAttemptService.getAttemptsByLearnerId(learnerId);
    }

    @GetMapping("/quiz/{quiz_id}")
    public List<QuizAttemptDto> getAttemptsByQuiz(@PathVariable String quiz_id) {
        return quizAttemptService.getAttemptsByQuizId(quiz_id);
    }

    @PutMapping("/update/{attempt_id}")
    public QuizAttemptDto updateQuizAttempt(@PathVariable String attempt_id, @RequestBody QuizAttempt quizAttempt) {
        return quizAttemptService.UpdateQuizAttempt(attempt_id, quizAttempt);
    }

    @DeleteMapping("/delete/{attempt_id}")
    public String deleteQuizAttempt(@PathVariable String attempt_id) {
        return quizAttemptService.delete(attempt_id);
    }
}
