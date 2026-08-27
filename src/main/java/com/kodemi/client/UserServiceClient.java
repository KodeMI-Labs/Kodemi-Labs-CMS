package com.kodemi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kodemi.dto.reviewsandratings.quizDto.LearnerResponseDTO;

 

@FeignClient(name = "user-service")
public interface UserServiceClient {

	//@GetMapping("/learner/{userId}")
	//LearnerResponseDTO getLearnerById(@PathVariable("userId") String userId);
	@GetMapping("/api/v1/learner/learner/{userId}")
    LearnerResponseDTO getLearnerById(@PathVariable("userId") String userId);
}