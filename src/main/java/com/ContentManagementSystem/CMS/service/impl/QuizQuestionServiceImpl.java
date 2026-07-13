package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizQuestionDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.Quiz;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizOption;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizQuestion;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizOptionRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizQuestionRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizRepository;
import com.ContentManagementSystem.CMS.service.QuizQuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizOptionRepository quizOptionRepository;
    private final QuizRepository quizRepository;

    public QuizQuestionServiceImpl(QuizQuestionRepository quizQuestionRepository,
                                   QuizOptionRepository quizOptionRepository,
                                   QuizRepository quizRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizOptionRepository = quizOptionRepository;
        this.quizRepository = quizRepository;
    }

    private void recalculateTotalMarks(String quiz_id) {
        if (quiz_id == null) return;
        Quiz quiz = quizRepository.findById(quiz_id);
        if (quiz == null) return;
        List<QuizQuestion> questions = quizQuestionRepository.findAll();
        int total = 0;
        for (QuizQuestion q : questions) {
            if (quiz_id.equals(q.getQuiz_id()) && q.getMarks() != null) {
                total += q.getMarks();
            }
        }
        quiz.setTotalMarks(total);
        quizRepository.save(quiz);
    }

    @Override
    public QuizQuestion createQuizQuestion(QuizQuestionDto quizQuestionDto) {
        QuizQuestion quizQuestion = new QuizQuestion();
        BeanUtils.copyProperties(quizQuestionDto, quizQuestion);
        quizQuestion.setQuestionId(UUID.randomUUID().toString());
        quizQuestionRepository.save(quizQuestion);
        recalculateTotalMarks(quizQuestion.getQuiz_id());
        return quizQuestion;
    }

    @Override
    public String createQuizQuestion(QuizQuestion quizQuestion) {
        quizQuestion.setQuestionId(UUID.randomUUID().toString());
        quizQuestionRepository.save(quizQuestion);
        recalculateTotalMarks(quizQuestion.getQuiz_id());
        return "Quiz Question Created Successfully";
    }

    @Override
    public QuizQuestionDto getQuizQuestion(String questionId) {
        QuizQuestion quizQuestion = quizQuestionRepository.findById(questionId);
        if (quizQuestion == null) {
            throw new ResourceNotFoundException("Quiz Question not found with id: " + questionId);
        }
        QuizQuestionDto quizQuestionDto = new QuizQuestionDto();
        BeanUtils.copyProperties(quizQuestion, quizQuestionDto);
        return quizQuestionDto;
    }

    @Override
    public List<QuizQuestionDto> getAllQuizQuestion() {
        List<QuizQuestion> questionList = quizQuestionRepository.findAll();
        List<QuizQuestionDto> questionDtoList = new ArrayList<>();
        for (QuizQuestion quizQuestion : questionList) {
            QuizQuestionDto quizQuestionDto = new QuizQuestionDto();
            BeanUtils.copyProperties(quizQuestion, quizQuestionDto);
            questionDtoList.add(quizQuestionDto);
        }
        return questionDtoList;
    }

    @Override
    public List<QuizQuestionDto> getQuestionsByQuizId(String quiz_id) {
        List<QuizQuestion> all = quizQuestionRepository.findAll();
        List<QuizQuestionDto> result = new ArrayList<>();
        for (QuizQuestion q : all) {
            if (quiz_id.equals(q.getQuiz_id())) {
                QuizQuestionDto dto = new QuizQuestionDto();
                BeanUtils.copyProperties(q, dto);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public String updateQuiz(String questionId, QuizQuestion quizQuestion) {
        QuizQuestion existing = quizQuestionRepository.findById(questionId);
        if (existing == null) {
            throw new ResourceNotFoundException("Quiz Question not found with id: " + questionId);
        }
        existing.setQuiz_id(quizQuestion.getQuiz_id());
        existing.setQuestion(quizQuestion.getQuestion());
        existing.setDifficultyLevel(quizQuestion.getDifficultyLevel());
        existing.setMarks(quizQuestion.getMarks());
        existing.setSequenceNo(quizQuestion.getSequenceNo());
        quizQuestionRepository.save(existing);
        return "Quiz Question Updated Successfully";
    }

    @Override
    public String delete(String questionId) {
        QuizQuestion quizQuestion = quizQuestionRepository.findById(questionId);
        if (quizQuestion == null) {
            throw new ResourceNotFoundException("Quiz Question not found with id: " + questionId);
        }
        // Cascade delete — remove all options for this question first
        List<QuizOption> options = quizOptionRepository.findAll();
        for (QuizOption option : options) {
            if (questionId.equals(option.getQuestionId())) {
                quizOptionRepository.delete(option.getOptionId());
            }
        }
        quizQuestionRepository.delete(questionId);
        return "Quiz Question and its Options Deleted Successfully";
    }
}
