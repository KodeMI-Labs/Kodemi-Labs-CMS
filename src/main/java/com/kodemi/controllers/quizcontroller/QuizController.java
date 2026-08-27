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

import com.kodemi.dto.reviewsandratings.quizDto.QuizDto;
import com.kodemi.model.quizmodel.Quiz;
import com.kodemi.service.QuizService;

import lombok.RequiredArgsConstructor;

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
    @GetMapping("/trainer/{trainer_id}")
    public List<QuizDto> getQuizzesByTrainer(@PathVariable String trainer_id) {
        return quizService.getQuizzesByTrainer(trainer_id);
    }
}
