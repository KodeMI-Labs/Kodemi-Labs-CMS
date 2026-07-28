package com.ContentManagementSystem.CMS.controller.quizcontroller;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizDto;
import com.ContentManagementSystem.CMS.model.quizmodel.Quiz;
import com.ContentManagementSystem.CMS.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/create")
    public String createQuiz(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @PostMapping("/create-dto")
    public Quiz createQuizDto(@RequestBody QuizDto quizDto) {
        return quizService.createQuiz(quizDto);
    }

    @GetMapping("/{quiz_id}")
    public QuizDto getQuizById(@PathVariable String quiz_id) {
        return quizService.findQuizId(quiz_id);
    }

    @GetMapping("/all")
    public List<QuizDto> getAllQuiz() {
        return quizService.getAllQuiz();
    }

    @PutMapping("/update/{quiz_id}")
    public String updateQuiz(@PathVariable String quiz_id, @RequestBody Quiz quiz) {
        return quizService.UpdateQuiz(quiz_id, quiz);
    }

    @DeleteMapping("/delete/{quiz_id}")
    public String deleteQuiz(@PathVariable String quiz_id) {
        return quizService.delete(quiz_id);
    }
}
