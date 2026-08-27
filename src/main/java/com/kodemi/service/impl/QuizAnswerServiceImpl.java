package com.kodemi.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.quizDto.QuizAnswerDto;
import com.kodemi.model.quizmodel.QuizAnswer;
import com.kodemi.repository.quizrepository.QuizAnswerRepository;
import com.kodemi.service.QuizAnswerService;

@Service
public class QuizAnswerServiceImpl implements QuizAnswerService {

    private final QuizAnswerRepository quizAnswerRepository;

    public QuizAnswerServiceImpl(QuizAnswerRepository quizAnswerRepository) {
        this.quizAnswerRepository = quizAnswerRepository;
    }

    @Override
    public QuizAnswer createQuizAnswer(QuizAnswerDto quizAnswerDto) {
        QuizAnswer quizAnswer = new QuizAnswer();
        BeanUtils.copyProperties(quizAnswerDto, quizAnswer);
        quizAnswer.setAnswer_id(UUID.randomUUID().toString());
        quizAnswer.setAnsweredAt(LocalDateTime.now());
        quizAnswerRepository.save(quizAnswer);
        return quizAnswer;
    }

    @Override
    public String createQuizAnswer(QuizAnswer quizAnswer) {
        quizAnswer.setAnswer_id(UUID.randomUUID().toString());
        quizAnswer.setAnsweredAt(LocalDateTime.now());
        quizAnswerRepository.save(quizAnswer);
        return "Quiz Answer Created Successfully";
    }

    @Override
    public QuizAnswerDto getQuizAnswerId(String answer_id) {
        QuizAnswer quizAnswer = quizAnswerRepository.findById(answer_id);
        if (quizAnswer == null) {
            throw new ResourceNotFoundException("Quiz Answer not found with id: " + answer_id);
        }
        QuizAnswerDto quizAnswerDto = new QuizAnswerDto();
        BeanUtils.copyProperties(quizAnswer, quizAnswerDto);
        return quizAnswerDto;
    }

    @Override
    public List<QuizAnswerDto> getAllQuizAnswer() {
        List<QuizAnswer> answerList = quizAnswerRepository.findAll();
        List<QuizAnswerDto> answerDtoList = new ArrayList<>();
        for (QuizAnswer quizAnswer : answerList) {
            QuizAnswerDto quizAnswerDto = new QuizAnswerDto();
            BeanUtils.copyProperties(quizAnswer, quizAnswerDto);
            answerDtoList.add(quizAnswerDto);
        }
        return answerDtoList;
    }

    @Override
    public List<QuizAnswerDto> getAnswersByAttemptId(String attempt_id) {
        List<QuizAnswer> all = quizAnswerRepository.findAll();
        List<QuizAnswerDto> result = new ArrayList<>();
        for (QuizAnswer answer : all) {
            if (attempt_id.equals(answer.getAttempt_id())) {
                QuizAnswerDto dto = new QuizAnswerDto();
                BeanUtils.copyProperties(answer, dto);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<QuizAnswerDto> getAnswersByQuestionId(String question_id) {
        List<QuizAnswer> all = quizAnswerRepository.findAll();
        List<QuizAnswerDto> result = new ArrayList<>();
        for (QuizAnswer answer : all) {
            if (question_id.equals(answer.getQuestion_id())) {
                QuizAnswerDto dto = new QuizAnswerDto();
                BeanUtils.copyProperties(answer, dto);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public String UpdateQuizAnswer(String answer_id, QuizAnswer quizAnswer) {
        QuizAnswer existing = quizAnswerRepository.findById(answer_id);
        if (existing == null) {
            throw new ResourceNotFoundException("Quiz Answer not found with id: " + answer_id);
        }
        existing.setAttempt_id(quizAnswer.getAttempt_id());
        existing.setQuestion_id(quizAnswer.getQuestion_id());
        existing.setSelectedOption_id(quizAnswer.getSelectedOption_id());
        existing.setCorrect(quizAnswer.getCorrect());
        quizAnswerRepository.save(existing);
        return "Quiz Answer Updated Successfully";
    }

    @Override
    public String delete(String answer_id) {
        QuizAnswer quizAnswer = quizAnswerRepository.findById(answer_id);
        if (quizAnswer == null) {
            throw new ResourceNotFoundException("Quiz Answer not found with id: " + answer_id);
        }
        quizAnswerRepository.delete(answer_id);
        return "Quiz Answer Deleted Successfully";
    }
}
