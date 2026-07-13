package com.ContentManagementSystem.CMS.controller.quizcontroller;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizCategoryDto;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizCategory;
import com.ContentManagementSystem.CMS.service.QuizCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
