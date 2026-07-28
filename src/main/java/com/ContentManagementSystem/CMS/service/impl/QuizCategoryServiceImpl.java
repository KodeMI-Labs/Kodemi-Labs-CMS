package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.quizDto.QuizCategoryDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizCategory;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizCategoryRepository;
import com.ContentManagementSystem.CMS.service.QuizCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuizCategoryServiceImpl implements QuizCategoryService {

    private final QuizCategoryRepository quizCategoryRepository;

    public QuizCategoryServiceImpl(QuizCategoryRepository quizCategoryRepository) {
        this.quizCategoryRepository = quizCategoryRepository;
    }

    @Override
    public QuizCategory createQuizCategory(QuizCategoryDto quizCategoryDto) {
        QuizCategory quizCategory = new QuizCategory();
        BeanUtils.copyProperties(quizCategoryDto, quizCategory);
        quizCategory.setCategoryId(UUID.randomUUID().toString());
        quizCategoryRepository.save(quizCategory);
        return quizCategory;
    }

    @Override
    public String createQuizCategory(QuizCategory quizCategory) {
        quizCategory.setCategoryId(UUID.randomUUID().toString());
        quizCategoryRepository.save(quizCategory);
        return "Quiz Category Created Successfully";
    }

    @Override
    public QuizCategoryDto getQuizCategory(String categoryId) {
        QuizCategory quizCategory = quizCategoryRepository.findById(categoryId);
        if (quizCategory == null) {
            throw new ResourceNotFoundException("Quiz Category not found with id: " + categoryId);
        }
        QuizCategoryDto quizCategoryDto = new QuizCategoryDto();
        BeanUtils.copyProperties(quizCategory, quizCategoryDto);
        return quizCategoryDto;
    }

    @Override
    public List<QuizCategoryDto> getAllQuizCategory() {
        List<QuizCategory> categoryList = quizCategoryRepository.findAll();
        List<QuizCategoryDto> categoryDtoList = new ArrayList<>();
        for (QuizCategory quizCategory : categoryList) {
            QuizCategoryDto quizCategoryDto = new QuizCategoryDto();
            BeanUtils.copyProperties(quizCategory, quizCategoryDto);
            categoryDtoList.add(quizCategoryDto);
        }
        return categoryDtoList;
    }

    @Override
    public String UpdateQuizCategory(String categoryId, QuizCategory quizCategory) {
        QuizCategory existing = quizCategoryRepository.findById(categoryId);
        if (existing == null) {
            throw new ResourceNotFoundException("Quiz Category not found with id: " + categoryId);
        }
        existing.setCategoryName(quizCategory.getCategoryName());
        existing.setDescription(quizCategory.getDescription());
        existing.setActive(quizCategory.getActive());
        quizCategoryRepository.save(existing);
        return "Quiz Category Updated Successfully";
    }

    @Override
    public String delete(String categoryId) {
        QuizCategory quizCategory = quizCategoryRepository.findById(categoryId);
        if (quizCategory == null) {
            throw new ResourceNotFoundException("Quiz Category not found with id: " + categoryId);
        }
        quizCategoryRepository.delete(categoryId);
        return "Quiz Category Deleted Successfully";
    }
}
