package com.ContentManagementSystem.CMS.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Fallback for CourseServiceClient.
 * Used when Course MS is unreachable — returns true to allow reviews
 * when course validation cannot be performed (graceful degradation).
 *
 * If you want strict validation, change return value to false.
 */
@Component
public class CourseServiceClientFallback implements CourseServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceClientFallback.class);

    @Override
    public boolean courseExists(String courseId) {
        log.warn("Course MS is unreachable. Falling back: assuming course {} exists.", courseId);
        // Returning true = graceful degradation (review is allowed even if MS is down)
        // Change to false if you want strict validation
        return true;
    }
}
