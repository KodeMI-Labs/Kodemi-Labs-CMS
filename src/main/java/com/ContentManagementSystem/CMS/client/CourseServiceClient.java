package com.ContentManagementSystem.CMS.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign Client for communicating with Course Microservice.
 * Validates that a course exists before creating a review.
 *
 * Set course-ms.url in application.yaml to point to your Course MS base URL.
 */
@FeignClient(name = "course-service", url = "${course-ms.url}", fallback = CourseServiceClientFallback.class)
public interface CourseServiceClient {

    /**
     * Checks if a course exists by courseId.
     * Course MS should expose: GET /courses/{courseId}/exists -> boolean
     * If the endpoint returns 404 or false, the review will be rejected.
     */
    @GetMapping("/courses/{courseId}/exists")
    boolean courseExists(@PathVariable("courseId") String courseId);
}
