package com.kodemi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", fallback = CourseServiceClientFallback.class)
public interface CourseServiceClient {

	@GetMapping("/courses/{courseId}/exists")
	boolean courseExists(@PathVariable("courseId") String courseId);
}