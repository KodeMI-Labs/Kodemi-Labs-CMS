package com.ContentManagementSystem.CMS.controller.quizcontroller;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizQuestionDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizQuestion;
import com.ContentManagementSystem.CMS.service.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
