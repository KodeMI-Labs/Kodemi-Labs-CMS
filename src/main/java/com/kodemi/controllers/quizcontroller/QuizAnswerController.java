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

import com.kodemi.dto.reviewsandratings.quizDto.QuizAnswerDto;
import com.kodemi.model.quizmodel.QuizAnswer;
import com.kodemi.service.QuizAnswerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz-answer")
@RequiredArgsConstructor
public class QuizAnswerController {
    private final QuizAnswerService quizAnswerService;
    @PostMapping("/create")
    public String createQuizAnswer(@RequestBody QuizAnswer quizAnswer) {
        return quizAnswerService.createQuizAnswer(quizAnswer);
    }

    @PostMapping("/create-dto")
    public QuizAnswer createQuizAnswerDto(@RequestBody QuizAnswerDto quizAnswerDto) {
        return quizAnswerService.createQuizAnswer(quizAnswerDto);
    }

    @GetMapping("/{answer_id}")
    public QuizAnswerDto getQuizAnswerById(@PathVariable String answer_id) {
        return quizAnswerService.getQuizAnswerId(answer_id);
    }

    @GetMapping("/all")
    public List<QuizAnswerDto> getAllQuizAnswer() {
        return quizAnswerService.getAllQuizAnswer();
    }

    @GetMapping("/attempt/{attempt_id}")
    public List<QuizAnswerDto> getAnswersByAttempt(@PathVariable String attempt_id) {
        return quizAnswerService.getAnswersByAttemptId(attempt_id);
    }

    @GetMapping("/question/{question_id}")
    public List<QuizAnswerDto> getAnswersByQuestion(@PathVariable String question_id) {
        return quizAnswerService.getAnswersByQuestionId(question_id);
    }

    @PutMapping("/update/{answer_id}")
    public String updateQuizAnswer(@PathVariable String answer_id, @RequestBody QuizAnswer quizAnswer) {
        return quizAnswerService.UpdateQuizAnswer(answer_id, quizAnswer);
    }
    @DeleteMapping("/delete/{answer_id}")
    public String deleteQuizAnswer(@PathVariable String answer_id) {
        return quizAnswerService.delete(answer_id);
    }
}
