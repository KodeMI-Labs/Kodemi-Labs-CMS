package com.ContentManagementSystem.CMS.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamoDbTableInitializer {

    private final AmazonDynamoDB amazonDynamoDB;
    private static final List<String[]> TABLES = List.of(
            new String[]{"Banner",       "banner_Id"},
            new String[]{"Blog",         "blog_Id"},
            new String[]{"FAQ",          "faq_Id"},
            new String[]{"Media",        "meta_Id"},
            new String[]{"Notes",        "note_id"},
            new String[]{"SEO",          "seo_id"},
            new String[]{"StaticPages",  "page_id"},
            new String[]{"Quiz",         "quiz_id"},
            new String[]{"QuizCategory", "category_id"},
            new String[]{"QuizQuestion", "question_id"},
            new String[]{"QuizOption",   "option_id"},
            new String[]{"QuizAttempt",  "attempt_id"},
            new String[]{"QuizAnswer",              "answer_id"},
            new String[]{"CourseReview",             "review_id"},
            new String[]{"CourseRatingSummary",      "course_id"},
            new String[]{"InstructorReview",         "review_id"},
            new String[]{"InstructorRatingSummary",  "instructor_id"},
            new String[]{"QuizAnalytics",            "analytics_id"},
            new String[]{"QuizStudentResult",        "result_id"},
            new String[]{"Testimonial",              "testimonial_id"}
    );

    @PostConstruct
    public void createTablesIfNotExist() {
        List<String> existing;
        try {
            existing = amazonDynamoDB.listTables().getTableNames();
        } catch (Exception e) {
            log.warn("Could not connect to DynamoDB at startup — tables will not be auto-created. Error: {}", e.getMessage());
            return;
        }

        for (String[] entry : TABLES) {
            String tableName = entry[0];
            String hashKey   = entry[1];

            if (existing.contains(tableName)) {
                log.info("DynamoDB table already exists: {}", tableName);
                // For CourseReview, ensure GSI exists
                if ("CourseReview".equals(tableName)) {
                    ensureCourseReviewGsi();
                }
                continue;
            }

            try {
                if ("CourseReview".equals(tableName)) {
                    createCourseReviewTable();
                } else {
                    amazonDynamoDB.createTable(new CreateTableRequest()
                            .withTableName(tableName)
                            .withKeySchema(new KeySchemaElement(hashKey, KeyType.HASH))
                            .withAttributeDefinitions(new AttributeDefinition(hashKey, ScalarAttributeType.S))
                            .withBillingMode(BillingMode.PAY_PER_REQUEST));
                }
                log.info("Created DynamoDB table: {}", tableName);
            } catch (ResourceInUseException e) {
                log.info("DynamoDB table already exists (race): {}", tableName);
            } catch (Exception e) {
                log.warn("Failed to create table {}: {}", tableName, e.getMessage());
            }
        }
    }

    private void createCourseReviewTable() {
        amazonDynamoDB.createTable(new CreateTableRequest()
                .withTableName("CourseReview")
                .withKeySchema(new KeySchemaElement("review_id", KeyType.HASH))
                .withAttributeDefinitions(
                        new AttributeDefinition("review_id", ScalarAttributeType.S),
                        new AttributeDefinition("course_id", ScalarAttributeType.S)
                )
                .withGlobalSecondaryIndexes(new GlobalSecondaryIndex()
                        .withIndexName("course-id-index")
                        .withKeySchema(new KeySchemaElement("course_id", KeyType.HASH))
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                )
                .withBillingMode(BillingMode.PAY_PER_REQUEST));
        log.info("Created DynamoDB table with GSI: CourseReview (course-id-index)");
    }

    private void ensureCourseReviewGsi() {
        try {
            TableDescription desc = amazonDynamoDB.describeTable("CourseReview").getTable();
            List<GlobalSecondaryIndexDescription> gsis = desc.getGlobalSecondaryIndexes();
            boolean gsiExists = gsis != null && gsis.stream()
                    .anyMatch(g -> "course-id-index".equals(g.getIndexName()));
            if (!gsiExists) {
                log.warn("CourseReview table exists but GSI 'course-id-index' is missing. " +
                        "Please delete and recreate the table to add the GSI, or add it manually via AWS console.");
            } else {
                log.info("CourseReview GSI 'course-id-index' is present.");
            }
        } catch (Exception e) {
            log.warn("Could not verify CourseReview GSI: {}", e.getMessage());
        }
    }
}
