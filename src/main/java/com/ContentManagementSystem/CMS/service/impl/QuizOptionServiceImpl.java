package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizOptionDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizOption;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizOptionRepository;
import com.ContentManagementSystem.CMS.service.QuizOptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuizOptionServiceImpl implements QuizOptionService {

    private final QuizOptionRepository quizOptionRepository;

    public QuizOptionServiceImpl(QuizOptionRepository quizOptionRepository) {
        this.quizOptionRepository = quizOptionRepository;
    }

    @Override
    public QuizOption createQuizOptionService(QuizOptionDto quizOptionDto) {
        QuizOption quizOption = new QuizOption();
        BeanUtils.copyProperties(quizOptionDto, quizOption);
        quizOption.setOptionId(UUID.randomUUID().toString());
        quizOptionRepository.save(quizOption);
        return quizOption;
    }

    @Override
    public String createQuizOptionService(QuizOption quizOption) {
        quizOption.setOptionId(UUID.randomUUID().toString());
        quizOptionRepository.save(quizOption);
        return "Quiz Option Created Successfully";
    }

    @Override
    public QuizOptionDto getQuizOption(String optionId) {
        QuizOption quizOption = quizOptionRepository.findById(optionId);
        if (quizOption == null) {
            throw new ResourceNotFoundException("Quiz Option not found with id: " + optionId);
        }
        QuizOptionDto quizOptionDto = new QuizOptionDto();
        BeanUtils.copyProperties(quizOption, quizOptionDto);
        return quizOptionDto;
    }

    @Override
    public List<QuizOption> getAllQuizOption() {
        return quizOptionRepository.findAll();
    }

    @Override
    public List<QuizOption> getOptionsByQuestionId(String questionId) {
        List<QuizOption> all = quizOptionRepository.findAll();
        List<QuizOption> result = new ArrayList<>();
        for (QuizOption option : all) {
            if (questionId.equals(option.getQuestionId())) {
                result.add(option);
            }
        }
        return result;
    }

    @Override
    public String updateQuiz(String optionId, QuizOption quizOption) {
        QuizOption existing = quizOptionRepository.findById(optionId);
        if (existing == null) {
            throw new ResourceNotFoundException("Quiz Option not found with id: " + optionId);
        }
        existing.setQuestionId(quizOption.getQuestionId());
        existing.setOptionText(quizOption.getOptionText());
        existing.setCorrect(quizOption.getCorrect());
        quizOptionRepository.save(existing);
        return "Quiz Option Updated Successfully";
    }

    @Override
    public String delete(String optionId) {
        QuizOption quizOption = quizOptionRepository.findById(optionId);
        if (quizOption == null) {
            throw new ResourceNotFoundException("Quiz Option not found with id: " + optionId);
        }
        quizOptionRepository.delete(optionId);
        return "Quiz Option Deleted Successfully";
    }
}
