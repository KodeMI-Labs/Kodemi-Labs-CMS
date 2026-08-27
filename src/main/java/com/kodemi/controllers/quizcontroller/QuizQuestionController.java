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

import com.kodemi.dto.reviewsandratings.quizDto.QuizQuestionDto;
import com.kodemi.model.quizmodel.QuizQuestion;
import com.kodemi.service.QuizQuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz-question")
@RequiredArgsConstructor
public class QuizQuestionController {
    private final QuizQuestionService quizQuestionService;
    @PostMapping("/create")
    public String createQuizQuestion(@RequestBody QuizQuestion quizQuestion) {
        return quizQuestionService.createQuizQuestion(quizQuestion);
    }
    @PostMapping("/create-dto")
    public QuizQuestion createQuizQuestionDto(@RequestBody QuizQuestionDto quizQuestionDto) {
        return quizQuestionService.createQuizQuestion(quizQuestionDto);
    }
    @GetMapping("/{questionId}")
    public QuizQuestionDto getQuizQuestionById(@PathVariable String questionId) {
        return quizQuestionService.getQuizQuestion(questionId);
    }
    @GetMapping("/all")
    public List<QuizQuestionDto> getAllQuizQuestion() {
        return quizQuestionService.getAllQuizQuestion();
    }
    @GetMapping("/quiz/{quiz_id}")
    public List<QuizQuestionDto> getQuestionsByQuiz(@PathVariable String quiz_id) {
        return quizQuestionService.getQuestionsByQuizId(quiz_id);
    }
    @PutMapping("/update/{questionId}")
    public String updateQuizQuestion(@PathVariable String questionId, @RequestBody QuizQuestion quizQuestion) {
        return quizQuestionService.updateQuiz(questionId, quizQuestion);
    }
    @DeleteMapping("/delete/{questionId}")
    public String deleteQuizQuestion(@PathVariable String questionId) {
        return quizQuestionService.delete(questionId);
    }
}
