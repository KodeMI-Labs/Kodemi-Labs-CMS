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

import com.kodemi.dto.reviewsandratings.quizDto.QuizStudentResultDto;
import com.kodemi.model.quizmodel.QuizStudentResult;
import com.kodemi.service.QuizStudentResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz-student-result")
@RequiredArgsConstructor
public class QuizStudentResultController {
    private final QuizStudentResultService quizStudentResultService;
    @PostMapping("/create")
    public String createResult(@RequestBody QuizStudentResult result) {
        return quizStudentResultService.createResult(result);
    }
    @PostMapping("/create-dto")
    public QuizStudentResult createResultDto(@RequestBody QuizStudentResultDto dto) {
        return quizStudentResultService.createResult(dto);
    }
    @GetMapping("/{result_id}")
    public QuizStudentResultDto getResultById(@PathVariable String result_id) {
        return quizStudentResultService.getResultById(result_id);
    }
    @GetMapping("/all")
    public List<QuizStudentResultDto> getAllResults() {
        return quizStudentResultService.getAllResults();
    }
    @GetMapping("/quiz/{quiz_id}")
    public List<QuizStudentResultDto> getResultsByQuiz(@PathVariable String quiz_id) {
        return quizStudentResultService.getResultsByQuizId(quiz_id);
    }

    @GetMapping("/learner/{learner_id}")
    public List<QuizStudentResultDto> getResultsByLearner(@PathVariable String learner_id) {
        return quizStudentResultService.getResultsByLearnerId(learner_id);
    }
    @PutMapping("/update/{result_id}")
    public String updateResult(@PathVariable String result_id,
                               @RequestBody QuizStudentResult result) {
        return quizStudentResultService.updateResult(result_id, result);
    }

    @DeleteMapping("/delete/{result_id}")
    public String deleteResult(@PathVariable String result_id) {
        return quizStudentResultService.delete(result_id);
    }
    @PostMapping("/backfill")
    public String backfillStudentResults() {
        return quizStudentResultService.backfillStudentResults();
    }
}
