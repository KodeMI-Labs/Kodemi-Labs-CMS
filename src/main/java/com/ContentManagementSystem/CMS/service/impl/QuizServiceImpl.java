package com.ContentManagementSystem.CMS.service.impl;
import com.ContentManagementSystem.CMS.dto.quizDto.QuizDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.quizmodel.Quiz;
import com.ContentManagementSystem.CMS.model.quizmodel.QuizAnalytics;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizAnalyticsRepository;
import com.ContentManagementSystem.CMS.repository.quizrepository.QuizRepository;
import com.ContentManagementSystem.CMS.service.QuizService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final QuizAnalyticsRepository quizAnalyticsRepository;

    public QuizServiceImpl(QuizRepository quizRepository,
                           QuizAnalyticsRepository quizAnalyticsRepository) {
        this.quizRepository = quizRepository;
        this.quizAnalyticsRepository = quizAnalyticsRepository;
    }

    private void createDefaultAnalytics(String quiz_id, String quiz_title) {
        QuizAnalytics analytics = new QuizAnalytics();
        analytics.setAnalytics_id(UUID.randomUUID().toString());
        analytics.setQuiz_id(quiz_id);
        analytics.setQuiz_title(quiz_title);
        analytics.setTotal_students(0);
        analytics.setAttempted_count(0);
        analytics.setNot_attempted_count(0);
        analytics.setAvg_score_percent(0.0);
        analytics.setHighest_score_percent(0.0);
        analytics.setOverall_performance_percent(0.0);
        analytics.setCompleted_count(0);
        analytics.setIn_progress_count(0);
        analytics.setNot_started_count(0);
        analytics.setRange_90_100_percent(0.0);
        analytics.setRange_80_89_percent(0.0);
        analytics.setRange_70_79_percent(0.0);
        analytics.setRange_60_69_percent(0.0);
        analytics.setRange_50_59_percent(0.0);
        analytics.setCreated_at(LocalDateTime.now());
        analytics.setUpdated_at(LocalDateTime.now());
        quizAnalyticsRepository.save(analytics);
    }
    @Override
    public Quiz createQuiz(QuizDto quizDto){
        Quiz quiz=new Quiz();
        BeanUtils.copyProperties(quizDto,quiz);
        quiz.setQuiz_id(UUID.randomUUID().toString());
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdateAt(LocalDateTime.now());
        if (quiz.getTotalMarks() == null) quiz.setTotalMarks(0);
        if (quiz.getTotalAttempts() == null) quiz.setTotalAttempts(0);
        if (quiz.getAverageRating() == null) quiz.setAverageRating(0.0);
        if (quiz.getQuizDate() == null) quiz.setQuizDate(LocalDateTime.now());
        quizRepository.save(quiz);
        createDefaultAnalytics(quiz.getQuiz_id(), quiz.getTitle());
        return quiz;
    }
    @Override
    public String createQuiz(Quiz quiz){
        quiz.setQuiz_id(UUID.randomUUID().toString());
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdateAt(LocalDateTime.now());
        if (quiz.getTotalMarks() == null) quiz.setTotalMarks(0);
        if (quiz.getTotalAttempts() == null) quiz.setTotalAttempts(0);
        if (quiz.getAverageRating() == null) quiz.setAverageRating(0.0);
        if (quiz.getQuizDate() == null) quiz.setQuizDate(LocalDateTime.now());
        quizRepository.save(quiz);
        createDefaultAnalytics(quiz.getQuiz_id(), quiz.getTitle());
        return "Quiz Created Successfully";
    }
    @Override
    public QuizDto findQuizId(String quiz_id){
        Quiz quiz=quizRepository.findById(quiz_id);
        if(quiz==null){
            throw new ResourceNotFoundException("Quiz is not found with id: "+quiz_id);
        }
        if (quiz.getTotalMarks() == null) quiz.setTotalMarks(0);
        if (quiz.getTotalAttempts() == null) quiz.setTotalAttempts(0);
        if (quiz.getAverageRating() == null) quiz.setAverageRating(0.0);
        if (quiz.getQuizDate() == null) quiz.setQuizDate(quiz.getCreatedAt());
        QuizDto quizDto=new QuizDto();
        BeanUtils.copyProperties(quiz,quizDto);
        return quizDto;
    }
    @Override
    public List<QuizDto> getAllQuiz(){
        List<Quiz> quizList=quizRepository.findAll();
        List<QuizDto> quizDtoList=new ArrayList<>();
        for(Quiz quiz:quizList){
            // apply defaults for legacy records with null fields
            if (quiz.getTotalMarks() == null) quiz.setTotalMarks(0);
            if (quiz.getTotalAttempts() == null) quiz.setTotalAttempts(0);
            if (quiz.getAverageRating() == null) quiz.setAverageRating(0.0);
            if (quiz.getQuizDate() == null) quiz.setQuizDate(quiz.getCreatedAt());
            QuizDto quizDto=new QuizDto();
            BeanUtils.copyProperties(quiz,quizDto);
            quizDtoList.add(quizDto);
        }
        return quizDtoList;
    }
    @Override
    public String UpdateQuiz(String quiz_id,Quiz quiz){
        Quiz existingQuiz=quizRepository.findById(quiz_id);
        if(existingQuiz==null){
            throw new ResourceNotFoundException("Quiz not found with id: "+quiz_id);
        }
        existingQuiz.setTitle(quiz.getTitle());
        existingQuiz.setDescription(quiz.getDescription());
        existingQuiz.setCategory(quiz.getCategory());
        existingQuiz.setTrainer_id(quiz.getTrainer_id());
        existingQuiz.setDurationMinutes(quiz.getDurationMinutes());
        existingQuiz.setTotalQuestion(quiz.getTotalQuestion());
        existingQuiz.setTotalMarks(quiz.getTotalMarks());
        existingQuiz.setQuizDate(quiz.getQuizDate());
        existingQuiz.setStatus(quiz.getStatus());
        existingQuiz.setUpdateAt(LocalDateTime.now());
        quizRepository.save(existingQuiz);
        return "Quiz Updated Successfully";
    }
    @Override
    public String delete(String quiz_id){
        Quiz quiz=quizRepository.findById(quiz_id);
        if(quiz==null){
            throw new ResourceNotFoundException("Quiz not found with id: "+quiz_id);
        }
        quizRepository.delete(quiz_id);
        return "Quiz Deleted Successfully";
    }
}
