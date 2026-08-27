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

import com.kodemi.dto.reviewsandratings.quizDto.QuizCategoryDto;
import com.kodemi.model.quizmodel.QuizCategory;
import com.kodemi.service.QuizCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz-category")
@RequiredArgsConstructor
public class QuizCategoryController {

    private final QuizCategoryService quizCategoryService;

    @PostMapping("/create")
    public String createQuizCategory(@RequestBody QuizCategory quizCategory) {
        return quizCategoryService.createQuizCategory(quizCategory);
    }
    @PostMapping("/create-dto")
    public QuizCategory createQuizCategoryDto(@RequestBody QuizCategoryDto quizCategoryDto) {
        return quizCategoryService.createQuizCategory(quizCategoryDto);
    }
    @GetMapping("/{categoryId}")
    public QuizCategoryDto getQuizCategoryById(@PathVariable String categoryId) {
        return quizCategoryService.getQuizCategory(categoryId);
    }
    @GetMapping("/all")
    public List<QuizCategoryDto> getAllQuizCategory() {
        return quizCategoryService.getAllQuizCategory();
    }
    @PutMapping("/update/{categoryId}")
    public String updateQuizCategory(@PathVariable String categoryId, @RequestBody QuizCategory quizCategory) {
        return quizCategoryService.UpdateQuizCategory(categoryId, quizCategory);
    }
    @DeleteMapping("/delete/{categoryId}")
    public String deleteQuizCategory(@PathVariable String categoryId) {
        return quizCategoryService.delete(categoryId);
    }
}
