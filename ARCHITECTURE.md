# 🏗️ CMS Application Architecture

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                         FRONTEND CLIENTS                             │
│  (React/Vue on port 5173 or 3000, ngrok tunnels for testing)         │
└──────────────────┬──────────────────────────────────────────────────┘
                   │ CORS Enabled
                   │ JWT Token
                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│                  CMS API (Spring Boot) - Port 8087                    │
│                                                                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                     REST Controllers                          │ │
│  │  • CourseReviewController                                    │ │
│  │  • BannerController, BlogController, etc.                   │ │
│  │  • QuizController, QuizQuestionController, etc.             │ │
│  │  • InstructorReviewController, etc.                         │ │
│  └─────────────┬──────────────────────────────────┬────────────┘ │
│                │                                  │                │
│                ▼                                  ▼                │
│  ┌────────────────────────────┐    ┌──────────────────────────┐  │
│  │   Service Layer            │    │  Feign Client            │  │
│  │  • CourseReviewService     │    │  • CourseServiceClient   │  │
│  │  • BannerService           │    │                          │  │
│  │  • BlogService, etc.       │    │  + Circuit Breaker       │  │
│  │                            │    │  + Fallback Mechanism    │  │
│  │  • Rating Aggregation      │    └────────┬─────────────────┘  │
│  │  • Auto-calculations       │             │                     │
│  └─────────────┬──────────────┘             │                     │
│                │                             │                     │
│                ▼                             ▼                     │
│  ┌────────────────────────────┐    ┌──────────────────────────┐  │
│  │   Repository Layer         │    │  Course Microservice     │  │
│  │  • CourseReviewRepository  │    │  • Port: 8088            │  │
│  │  • BannerRepository        │───┤  • Endpoint: /courses/   │  │
│  │  • BlogRepository, etc.    │    │    {courseId}/exists     │  │
│  │                            │    │                          │  │
│  │  (DynamoDB Mapper)         │    │  Fallback: Returns true  │  │
│  └─────────────┬──────────────┘    │  if unreachable          │  │
│                │                    └──────────────────────────┘  │
│  ┌─────────────┴──────────────────────────────────────────────┐   │
│  │              DynamoDB Configuration Layer                  │   │
│  │  • AWS Credentials Provider                               │   │
│  │  • Region: ap-south-1 (Mumbai)                            │   │
│  │  • Table Initializer (auto-creates 20 tables)             │   │
│  │  • DynamoDBMapper Bean                                    │   │
│  └────────────────┬─────────────────────────────────────────┘    │
└───────────────────┼──────────────────────────────────────────────┘
                    │
                    │ AWS SDK v1.12.700
                    │ HTTPS
                    │
                    ▼
    ┌─────────────────────────────────────────────────┐
    │         AWS DynamoDB (ap-south-1)               │
    │                                                  │
    │  ┌──────────────────────────────────────────┐  │
    │  │           Content Tables                 │  │
    │  │  • Banner      • Blog         • FAQ      │  │
    │  │  • Media       • Notes        • SEO      │  │
    │  │  • StaticPages • Testimonial             │  │
    │  └──────────────────────────────────────────┘  │
    │                                                  │
    │  ┌──────────────────────────────────────────┐  │
    │  │           Quiz Tables                    │  │
    │  │  • Quiz        • QuizCategory            │  │
    │  │  • QuizQuestion • QuizOption             │  │
    │  │  • QuizAttempt • QuizAnswer              │  │
    │  │  • QuizAnalytics • QuizStudentResult     │  │
    │  └──────────────────────────────────────────┘  │
    │                                                  │
    │  ┌──────────────────────────────────────────┐  │
    │  │        Review & Rating Tables            │  │
    │  │  • CourseReview (with GSI)               │  │
    │  │  • CourseRatingSummary                   │  │
    │  │  • InstructorReview (with GSI)           │  │
    │  │  • InstructorRatingSummary               │  │
    │  └──────────────────────────────────────────┘  │
    │                                                  │
    │  ┌──────────────────────────────────────────┐  │
    │  │         Billing Model                    │  │
    │  │  • Pay-per-request                       │  │
    │  │  • On-demand scaling                     │  │
    │  │  • No provisioned capacity needed        │  │
    │  └──────────────────────────────────────────┘  │
    │                                                  │
    └─────────────────────────────────────────────────┘
```

---

## Request Flow: Create Review with Course Validation

```
1. Frontend sends POST request
   ↓
2. CourseReviewController.createReviewForCourse()
   ↓
3. SecurityFilter validates JWT token
   ↓
4. CourseReviewService.createReviewForCourse()
   ↓
5. Feign Client calls Course MS
   ├─ GET /courses/{courseId}/exists
   ├─ Course MS validates course exists
   └─ Returns: true or false
   ↓
6. If course exists:
   ├─ Validate rating (1-5)
   ├─ Generate UUID for review
   ├─ Save to DynamoDB (CourseReview table)
   ├─ Update CourseRatingSummary (auto-calculate average)
   └─ Return ReviewResponse
   ↓
7. If course NOT found:
   └─ Throw ResourceNotFoundException → 404
   ↓
8. If Course MS unreachable:
   ├─ Fallback mechanism activated
   ├─ Log: "Course MS is unreachable. Falling back..."
   ├─ Return: true (graceful degradation)
   └─ Review creation proceeds
   ↓
9. Response sent back to frontend
   ├─ Review ID
   ├─ Course ID
   ├─ Rating
   └─ Timestamps
```

---

## Data Flow: Query Reviews by Course (Efficient GSI)

```
1. Frontend sends GET request
   ↓
2. CourseReviewController.getReviewsByCourseId()
   ↓
3. SecurityFilter validates JWT
   ↓
4. CourseReviewService.getReviewsByCourse(courseId)
   ↓
5. CourseReviewRepository.findByCourseId(courseId)
   ├─ Create DynamoDBQueryExpression
   ├─ Specify index: "course-id-index" (Global Secondary Index)
   ├─ Set hash key: course_id
   ├─ Set consistent read: false (GSI only supports eventual consistency)
   └─ Execute query
   ↓
6. DynamoDB processes GSI query
   ├─ Looks up course_id in GSI partition
   ├─ Returns all matching review records
   └─ Much faster than full table scan
   ↓
7. Mapper converts DynamoDB items to CourseReview objects
   ↓
8. Service maps to ReviewResponse DTOs
   ↓
9. Response sent to frontend
   ├─ Array of reviews
   ├─ Each with all fields
   └─ JSON format
```

---

## Component Architecture: Detailed

```
┌─────────────────────────────────────────────────────────────────┐
│                        CMS Application                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  PRESENTATION LAYER (Controllers)                              │
│  ├─ CourseReviewController          [POST/GET/PUT/DELETE]      │
│  ├─ BannerController                [Content Management]        │
│  ├─ BlogController                  [Content Management]        │
│  ├─ QuizController                  [Quiz Management]           │
│  ├─ QuizQuestionController          [Quiz Content]              │
│  ├─ QuizAttemptController           [Student Attempts]          │
│  └─ ... (15+ more controllers)                                  │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  BUSINESS LOGIC LAYER (Services)                               │
│  ├─ CourseReviewService             [Interface]                │
│  ├─ CourseReviewServiceImpl          [Implementation]           │
│  │   ├─ createReviewForCourse()     [Feign validation]         │
│  │   ├─ getReviewsByCourse()        [GSI query]                │
│  │   └─ updateRatingSummary()       [Auto-calculation]         │
│  ├─ CourseRatingSummaryService      [Rating aggregation]       │
│  ├─ BannerService                   [Banner management]        │
│  ├─ QuizService                     [Quiz management]          │
│  └─ ... (15+ more services)                                    │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  DATA ACCESS LAYER (Repositories)                              │
│  ├─ CourseReviewRepository                                     │
│  │   ├─ save(CourseReview)          [Insert/Update]            │
│  │   ├─ findById(String)            [Get by PK]                │
│  │   ├─ findByCourseId(String)      [GSI query]                │
│  │   ├─ findAll()                   [Table scan]               │
│  │   └─ delete(String)              [Remove]                   │
│  ├─ CourseRatingSummaryRepository   [Rating lookup]            │
│  ├─ BannerRepository                [Banner data]              │
│  ├─ BlogRepository                  [Blog data]                │
│  └─ ... (15+ more repositories)                                │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  MODEL LAYER (Entities)                                        │
│  ├─ CourseReview                    [@DynamoDBTable]           │
│  │   ├─ review_id                  [Hash Key]                  │
│  │   ├─ course_id                  [GSI Partition Key]         │
│  │   ├─ learner_id, rating, comment [Attributes]               │
│  │   └─ ...                                                    │
│  ├─ CourseRatingSummary                                        │
│  ├─ Banner, Blog, FAQ, etc.                                    │
│  └─ ... (all entities with DynamoDB annotations)               │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  CROSS-CUTTING CONCERNS                                        │
│  ├─ Security Layer                                             │
│  │   ├─ JwtFilter           [Token validation]                 │
│  │   ├─ JwtUtil             [Token generation]                 │
│  │   └─ SecurityConfig      [Spring Security setup]            │
│  ├─ Microservice Integration                                   │
│  │   ├─ CourseServiceClient [Feign interface]                  │
│  │   └─ CourseServiceClientFallback [Circuit breaker]          │
│  ├─ Configuration                                              │
│  │   ├─ DynamoDbConfig      [DynamoDB setup]                   │
│  │   ├─ DynamoDbTableInitializer [Auto-create tables]          │
│  │   ├─ LocalDateTimeConverter [Custom serialization]          │
│  │   └─ CORS configuration  [Cross-origin requests]            │
│  ├─ Exception Handling                                         │
│  │   ├─ BadRequestException [400 errors]                       │
│  │   ├─ ResourceNotFoundException [404 errors]                  │
│  │   └─ GlobalExceptionHandler [Centralized]                   │
│  └─ Logging                                                    │
│      └─ logback-spring.xml  [Comprehensive logging]            │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  INFRASTRUCTURE LAYER                                          │
│  ├─ DynamoDBMapper                  [ORM for DynamoDB]         │
│  ├─ AWS SDK                         [AWS connectivity]         │
│  ├─ Feign Client                    [HTTP client for MS]       │
│  ├─ Spring Boot                     [Application framework]    │
│  └─ Maven                           [Build tool]               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Table Schema: CourseReview with GSI

```
Table: CourseReview
├─ Partition Key (Hash): review_id [String]
├─ Global Secondary Index: course-id-index
│  └─ Partition Key: course_id [String]
│     └─ Enables efficient queries by course
│
├─ Attributes:
│  ├─ review_id                   [String] - Unique review ID
│  ├─ course_id                   [String] - Course ID (GSI key)
│  ├─ learner_id                  [String] - Student ID
│  ├─ learner_name                [String] - Student name
│  ├─ learner_profile_image       [String] - Avatar URL
│  ├─ rating                      [Number] - 1-5 stars
│  ├─ review_comment              [String] - Review text
│  ├─ review_at                   [String] - Timestamp
│  ├─ edited                      [Boolean] - Edit flag
│  └─ approved                    [Boolean] - Approval flag
│
└─ Billing: Pay-per-request
   ├─ Write: ~1 WCU per review
   ├─ Read: ~0.5 RCU per query
   └─ Query cost depends on usage
```

---

## Error Handling & Fallback Flows

```
SCENARIO 1: Course MS is Available
────────────────────────────────────
User Request → Feign Client → Course MS
                  ↓
           Validation Success
                  ↓
        Save Review to DynamoDB
                  ↓
     Update CourseRatingSummary
                  ↓
           Return 201 Created

SCENARIO 2: Course MS is Down
──────────────────────────────
User Request → Feign Client → Fallback
                  ↓
    Log: "Course MS is unreachable"
                  ↓
    Return true (graceful degradation)
                  ↓
    Save Review to DynamoDB
                  ↓
    Update CourseRatingSummary
                  ↓
    Return 201 Created (note: fallback used)

SCENARIO 3: Invalid Rating
──────────────────────────
User Request → Service Validation
                  ↓
         Rating < 1 or > 5
                  ↓
    Throw BadRequestException
                  ↓
    Global Exception Handler
                  ↓
         Return 400 Bad Request
         With error message

SCENARIO 4: Course Not Found (Strict Mode)
───────────────────────────────────────────
User Request → Feign Client → Course MS
                  ↓
         Course not found
                  ↓
    Throw ResourceNotFoundException
                  ↓
    Global Exception Handler
                  ↓
         Return 404 Not Found

SCENARIO 5: DynamoDB Save Fails
────────────────────────────────
Service → DynamoDB
          ↓
    Connection error
          ↓
    AWS SDK exception
          ↓
    Service catches
          ↓
    Logs error
          ↓
    Throws custom exception
          ↓
    Return 500 Internal Error
```

---

## Data Persistence Flow

```
CREATE REVIEW
─────────────
1. ReviewRequest DTO arrives
   ├─ learner_id, name, image
   ├─ rating (1-5)
   └─ comment

2. Service generates:
   ├─ review_id = UUID.randomUUID()
   ├─ review_at = LocalDateTime.now()
   └─ edited = false, approved = false

3. CourseReview entity created
   ├─ All fields populated
   └─ Entity mapped to DynamoDB schema

4. DynamoDB save operation
   ├─ HashMap serialization (DynamoDB native format)
   ├─ Network transmission (HTTPS)
   ├─ AWS DynamoDB storage
   └─ Consistency: Eventually consistent (replicated)

5. CourseRatingSummary update
   ├─ Load existing summary (or create new)
   ├─ Increment: total_reviews, star_count
   ├─ Recalculate: average_rating
   └─ Save updated summary

6. Response mapping
   ├─ DynamoDB item → ReviewResponse DTO
   └─ Return to controller

7. HTTP Response
   ├─ Status: 201 Created
   ├─ Body: ReviewResponse JSON
   └─ Headers: Location, Content-Type

QUERY REVIEWS BY COURSE (GSI)
──────────────────────────────
1. User requests: GET /courses/{courseId}/reviews

2. Repository query
   ├─ Create DynamoDBQueryExpression
   ├─ Index: "course-id-index"
   ├─ HashKey value: courseId
   └─ Consistency: Eventual (GSI only)

3. DynamoDB processes
   ├─ Looks up GSI partition
   ├─ Retrieves all items with course_id
   ├─ Pagination support (if many items)
   └─ Fast query (efficient indexing)

4. Results mapping
   ├─ DynamoDB items → CourseReview entities
   ├─ CourseReview → ReviewResponse DTOs
   └─ Stream → List

5. HTTP Response
   ├─ Status: 200 OK
   ├─ Body: Array of ReviewResponse
   └─ Performance: ~200ms for typical queries
```

---

## Deployment Architecture

```
DEVELOPMENT
───────────
Laptop/Desktop
├─ Maven (build)
├─ Spring Boot (run)
├─ Java 17
└─ AWS DynamoDB (ap-south-1)

TESTING
───────
Docker Container
├─ Java 17 image
├─ Spring Boot app
├─ Environment variables
└─ AWS DynamoDB (ap-south-1)

PRODUCTION
──────────
Kubernetes / ECS
├─ Multiple pod replicas
├─ Load balancer (ALB)
├─ IAM roles (no credentials in code)
├─ CloudWatch logs
├─ Auto-scaling
└─ AWS DynamoDB (ap-south-1)
   ├─ Pay-per-request billing
   ├─ Global Tables (optional)
   ├─ Point-in-time recovery
   └─ Backups
```

---

## Technology Stack Summary

```
FRONTEND
────────
• React/Vue (port 5173/3000)
• REST API client
• JWT token storage

BACKEND
───────
• Spring Boot 3.5.13
• Java 17
• Maven 3.8+
• Embedded Tomcat

MICROSERVICES
─────────────
• Feign Client (HTTP calls)
• Spring Cloud OpenFeign
• Circuit Breaker (resilience)
• Fallback mechanism

DATA ACCESS
───────────
• AWS SDK 1.12.700
• DynamoDB mapper
• Annotation-based entity mapping
• Custom type converters

SECURITY
────────
• Spring Security
• JWT (JJWT)
• CORS configuration
• Input validation

LOGGING & MONITORING
────────────────────
• Logback
• SLF4J
• File rotation
• CloudWatch (optional)

INFRASTRUCTURE
──────────────
• AWS DynamoDB (pay-per-request)
• ap-south-1 region (Mumbai)
• Auto-scaling
• Eventually consistent reads
```

---

## Performance Characteristics

```
OPERATION LATENCY (Typical)
────────────────────────────
Create Review:
  ├─ Feign call to Course MS: 50-200ms
  ├─ Validate rating: <1ms
  ├─ Save to DynamoDB: 10-50ms
  ├─ Update summary: 10-50ms
  └─ Total: 100-300ms

Get Review by ID (Primary Key):
  ├─ DynamoDB lookup: 5-15ms
  ├─ Entity mapping: <1ms
  └─ Total: 5-20ms

Get Reviews by Course (GSI Query):
  ├─ GSI lookup: 20-50ms (depends on result size)
  ├─ Entity mapping: 1-10ms
  └─ Total: 30-100ms

Get All Reviews (Full Scan):
  ├─ DynamoDB scan: Depends on table size
  ├─ Pagination: Handle 1MB batches
  └─ Total: 100ms to several seconds

SCALABILITY
───────────
• RPS (Requests Per Second): Unlimited
• Concurrent connections: Unlimited
• Storage: Unlimited
• Item size: Max 400KB per item

COST (Approximate)
──────────────────
• Per million write units: $1.25
• Per million read units: $0.25
• No minimum charges
• Data transfer: $0.09 per GB out
```

---

This architecture ensures:
✅ Scalability
✅ Reliability
✅ Performance
✅ Security
✅ Maintainability

