package com.kodemi.controllers.quizcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.config.JwtUtil;
import com.kodemi.dto.reviewsandratings.quizDto.QuizAttemptDto;
import com.kodemi.model.quizmodel.QuizAttempt;
import com.kodemi.service.QuizAttemptService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz-attempt")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;
    private final JwtUtil jwtUtil;

    /** Extracts learnerId from the Authorization header; returns null if token is absent/invalid. */
    private String learnerIdFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                return jwtUtil.extractUserId(header.substring(7));
            } catch (Exception ignored) {}
        }
        return null;
    }

    @PostMapping("/create")
    public QuizAttemptDto createQuizAttempt(@RequestBody QuizAttempt quizAttempt,
                                    HttpServletRequest request) {
        // Ensure learnerId is always set — fall back to JWT subject if body omits it
        if (quizAttempt.getLearnerId() == null || quizAttempt.getLearnerId().isBlank()) {
            String jwtLearnerId = learnerIdFromRequest(request);
            if (jwtLearnerId != null) quizAttempt.setLearnerId(jwtLearnerId);
        }
        return quizAttemptService.createQuizAttempt(quizAttempt);
    }

    @PostMapping("/create-dto")
    public QuizAttempt createQuizAttemptDto(@RequestBody QuizAttemptDto quizAttemptDto,
                                            HttpServletRequest request) {
        // Ensure learnerId is always set — fall back to JWT subject if body omits it
        if (quizAttemptDto.getLearnerId() == null || quizAttemptDto.getLearnerId().isBlank()) {
            String jwtLearnerId = learnerIdFromRequest(request);
            if (jwtLearnerId != null) quizAttemptDto.setLearnerId(jwtLearnerId);
        }
        return quizAttemptService.createQuizAttempt(quizAttemptDto);
    }

    @GetMapping("/{attempt_id}")
    public QuizAttemptDto getQuizAttemptById(@PathVariable String attempt_id) {
        return quizAttemptService.getQuizAttemptId(attempt_id);
    }

    @GetMapping("/all")
    public List<QuizAttemptDto> getAllQuizAttempt() {
        return quizAttemptService.getAllQuizAttempt();
    }

    @GetMapping("/learner/{learnerId}")
    public List<QuizAttemptDto> getAttemptsByLearner(@PathVariable String learnerId) {
        return quizAttemptService.getAttemptsByLearnerId(learnerId);
    }

    @GetMapping("/quiz/{quiz_id}")
    public List<QuizAttemptDto> getAttemptsByQuiz(@PathVariable String quiz_id) {
        return quizAttemptService.getAttemptsByQuizId(quiz_id);
    }

    @PutMapping("/update/{attempt_id}")
    public QuizAttemptDto updateQuizAttempt(@PathVariable String attempt_id,
                                            @RequestBody QuizAttempt quizAttempt,
                                            HttpServletRequest request) {
        // Fill learnerId from JWT if not supplied in body
        if (quizAttempt.getLearnerId() == null || quizAttempt.getLearnerId().isBlank()) {
            String jwtLearnerId = learnerIdFromRequest(request);
            if (jwtLearnerId != null) quizAttempt.setLearnerId(jwtLearnerId);
        }
        return quizAttemptService.UpdateQuizAttempt(attempt_id, quizAttempt);
    }

    @PatchMapping("/patch/{attempt_id}")
    public QuizAttemptDto patchQuizAttempt(@PathVariable String attempt_id,
                                           @RequestBody QuizAttemptDto patchDto,
                                           HttpServletRequest request) {
        // Fill learnerId from JWT if not supplied in body
        if (patchDto.getLearnerId() == null || patchDto.getLearnerId().isBlank()) {
            String jwtLearnerId = learnerIdFromRequest(request);
            if (jwtLearnerId != null) patchDto.setLearnerId(jwtLearnerId);
        }
        return quizAttemptService.patchQuizAttempt(attempt_id, patchDto);
    }

    @DeleteMapping("/delete/{attempt_id}")
    public String deleteQuizAttempt(@PathVariable String attempt_id) {
        return quizAttemptService.delete(attempt_id);
    }
}
