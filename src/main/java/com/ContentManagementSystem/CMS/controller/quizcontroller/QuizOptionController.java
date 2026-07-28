package com.ContentManagementSystem.CMS.controller.quizcontroller;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizOptionDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizOption;
import com.ContentManagementSystem.CMS.service.QuizOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
