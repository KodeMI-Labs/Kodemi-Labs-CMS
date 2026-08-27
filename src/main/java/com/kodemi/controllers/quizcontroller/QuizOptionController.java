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

import com.kodemi.dto.reviewsandratings.quizDto.QuizOptionDto;
import com.kodemi.model.quizmodel.QuizOption;
import com.kodemi.service.QuizOptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz-option")
@RequiredArgsConstructor
public class QuizOptionController {

    private final QuizOptionService quizOptionService;

    @PostMapping("/create")
    public String createQuizOption(@RequestBody QuizOption quizOption) {
        return quizOptionService.createQuizOptionService(quizOption);
    }

    @PostMapping("/create-dto")
    public QuizOption createQuizOptionDto(@RequestBody QuizOptionDto quizOptionDto) {
        return quizOptionService.createQuizOptionService(quizOptionDto);
    }

    @GetMapping("/{optionId}")
    public QuizOptionDto getQuizOptionById(@PathVariable String optionId) {
        return quizOptionService.getQuizOption(optionId);
    }

    @GetMapping("/all")
    public List<QuizOption> getAllQuizOption() {
        return quizOptionService.getAllQuizOption();
    }

    @GetMapping("/question/{questionId}")
    public List<QuizOption> getOptionsByQuestion(@PathVariable String questionId) {
        return quizOptionService.getOptionsByQuestionId(questionId);
    }

    @PutMapping("/update/{optionId}")
    public String updateQuizOption(@PathVariable String optionId, @RequestBody QuizOption quizOption) {
        return quizOptionService.updateQuiz(optionId, quizOption);
    }

    @DeleteMapping("/delete/{optionId}")
    public String deleteQuizOption(@PathVariable String optionId) {
        return quizOptionService.delete(optionId);
    }
}
