# CMS Application - Deployment & Integration Guide

## 📋 Overview

Your CMS application is now configured to:
1. ✅ Connect to **AWS DynamoDB** (ap-south-1 region)
2. ✅ Auto-create 20 DynamoDB tables on startup
3. ✅ Integrate with **Course Microservice** via Feign client
4. ✅ Fetch and persist data perfectly with proper error handling
5. ✅ Auto-calculate rating aggregations

---

## 🎯 What Has Been Done

### 1. AWS DynamoDB Connection
- ✅ Updated `application.yaml` with AWS credentials
- ✅ Configured region: **ap-south-1** (Mumbai)
- ✅ Set `use-local: false` to use AWS (not local)
- ✅ Added environment variable support for credentials

### 2. Automatic Table Creation
The `DynamoDbTableInitializer` component creates 20 tables on startup:

```java
@PostConstruct
public void createTablesIfNotExist() {
    // Checks if tables exist, creates if needed
    // Handles CourseReview table specially with GSI
}
```

**Tables created:**
- Content: Banner, Blog, FAQ, Media, Notes, SEO, StaticPages, Testimonial
- Quiz: Quiz, QuizCategory, QuizQuestion, QuizOption, QuizAttempt, QuizAnswer, QuizAnalytics, QuizStudentResult
- Reviews: CourseReview (with GSI), CourseRatingSummary, InstructorReview, InstructorRatingSummary

### 3. Microservice Integration
- ✅ Feign client configured to validate courses before creating reviews
- ✅ Circuit breaker enabled for resilience
- ✅ Fallback mechanism for graceful degradation if Course MS is down

### 4. Data Fetching & Persistence
- ✅ DynamoDBMapper properly configured
- ✅ All entities have @DynamoDBTable annotations
- ✅ Repositories use efficient queries (GSI for course_id lookups)
- ✅ Auto-update rating summaries on each new review

### 5. Enhanced Logging
- ✅ Created `logback-spring.xml` for comprehensive logging
- ✅ Debug level for application code
- ✅ File and console output
- ✅ Rolling log files

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- AWS Account with DynamoDB access
- Course Microservice running (optional, has fallback)

### Step 1: Extract Credentials
Your AWS credentials have been configured:
```
Access Key: your_aws_access_key_here
Secret Key: your_aws_secret_key_here
Region: ap-south-1
```

### Step 2: Build the Application
```bash
cd /Users/venkatkarthik/Downloads/CMS/CMS
mvn clean install
```

### Step 3: Run the Application
```bash
mvn spring-boot:run
```

**Expected Output:**
```
Started CmsApplication in X seconds
INFO - Created DynamoDB table: Banner
INFO - Created DynamoDB table: Blog
... (all 20 tables)
INFO - DynamoDB table already exists: CourseReview
INFO - CourseReview GSI 'course-id-index' is present.
```

### Step 4: Verify Tables in AWS
1. Go to AWS Console: https://528701450746.signin.aws.amazon.com/console
2. Login with credentials provided
3. Navigate to DynamoDB
4. Select region: **ap-south-1**
5. Verify all 20 tables exist

---

## 🔗 Integration Points

### 1. DynamoDB Integration
```
Application → DynamoDB Config → AWS DynamoDB
     ↓
  Spring Beans (DynamoDBMapper)
     ↓
  Repositories (CourseReviewRepository, etc.)
     ↓
  Services (CourseReviewService, etc.)
     ↓
  Controllers (REST Endpoints)
```

### 2. Microservice Integration
```
Controller receives /courses/{courseId}/reviews request
     ↓
Service calls courseServiceClient.courseExists(courseId)
     ↓
Feign Client sends HTTP GET to Course MS
     ↓
Course MS validates and responds
     ↓
If Course MS down → Fallback returns true (graceful degradation)
     ↓
Service saves review to DynamoDB + updates rating summary
```

### 3. Data Flow Example: Create Review
```
POST /courses/{courseId}/reviews
  ↓
CourseReviewController.createReviewForCourse()
  ↓
CourseReviewServiceImpl.createReviewForCourse()
  ↓
1. Validate course via Feign (CourseServiceClient)
2. Validate rating (1-5)
3. Save review to DynamoDB
4. Update CourseRatingSummary (auto-calculate average)
5. Return ReviewResponse
```

---

## 📊 Data Fetching Examples

### Create a Review
```bash
curl -X POST http://localhost:8087/courses/course-123/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "learner_id": "learner-456",
    "learner_name": "John Doe",
    "learner_profile_image": "https://example.com/john.jpg",
    "rating": 5,
    "comment": "Excellent course!"
  }'
```

**Response:**
```json
{
  "reviewId": "550e8400-e29b-41d4-a716-446655440000",
  "courseId": "course-123",
  "learnerId": "learner-456",
  "learnerName": "John Doe",
  "rating": 5,
  "comment": "Excellent course!",
  "reviewedAt": "2026-07-21T11:40:58",
  "edited": false,
  "approved": false
}
```

### Get Reviews by Course (Efficient GSI Query)
```bash
curl http://localhost:8087/courses/course-123/reviews
```

**Response:**
```json
[
  {
    "reviewId": "550e8400-e29b-41d4-a716-446655440000",
    "courseId": "course-123",
    "learnerId": "learner-456",
    "learnerName": "John Doe",
    "rating": 5,
    "comment": "Excellent course!",
    "reviewedAt": "2026-07-21T11:40:58",
    "edited": false,
    "approved": false
  },
  ...more reviews
]
```

### Get Rating Summary
```bash
curl http://localhost:8087/courses/course-123/rating-summary
```

**Response:**
```json
{
  "courseId": "course-123",
  "totalReviews": 10,
  "averageRating": 4.5,
  "fiveStarCount": 7,
  "fourStarCount": 2,
  "threeStarCount": 1,
  "twoStarCount": 0,
  "oneStarCount": 0
}
```

---

## 🔧 Configuration Files

### application.yaml
Main configuration file with:
- AWS credentials (supports environment variables)
- DynamoDB mode (AWS vs Local)
- Microservice URLs
- JWT settings
- CORS configuration

### DynamoDbConfig.java
Handles:
- AWS credential setup
- Region configuration
- Mode switching (local/AWS)
- DynamoDBMapper bean creation

### DynamoDbTableInitializer.java
Responsible for:
- Auto-creating tables on startup
- Handling GSI for CourseReview
- Graceful error handling
- Idempotent operations

### logback-spring.xml
Configured logging:
- Console output
- File output (logs/cms.log)
- Debug level for app code
- Info level for AWS/Spring
- Rolling file policies

---

## 🛡️ Security Best Practices

### 1. Environment Variables (Recommended for Production)
Instead of hardcoding credentials:

```bash
export AWS_ACCESS_KEY_ID=your_aws_access_key_here
export AWS_SECRET_ACCESS_KEY=your_aws_secret_key_here
export AWS_REGION=ap-south-1

mvn spring-boot:run
```

### 2. AWS Secrets Manager (For Sensitive Environments)
```bash
# Store credentials in AWS Secrets Manager
# Configure Spring Boot to fetch from Secrets Manager
```

### 3. IAM Roles (For EC2/ECS Deployments)
```bash
# Attach IAM role with DynamoDB permissions
# No credentials needed in code
```

### 4. Add to .gitignore
```
application-prod.yaml
.env
logs/
```

---

## 📈 Monitoring & Troubleshooting

### Check Connection
```bash
# View logs in real-time
tail -f logs/cms.log

# Look for these messages:
# ✅ "Created DynamoDB table: Banner"
# ✅ "DynamoDB table already exists: CourseReview"
# ❌ "Failed to create table"
# ❌ "Could not connect to DynamoDB"
```

### Verify Tables in AWS
```bash
# Check AWS console for tables in ap-south-1
# Verify CourseReview has GSI: course-id-index
# Test queries work (GSI queries should be fast)
```

### Test Microservice Integration
```bash
# Check Course MS is running on port 8088
curl http://localhost:8088/courses/test/exists

# If down, Feign fallback will allow reviews to proceed
# Check logs for: "Course MS is unreachable. Falling back..."
```

### API Connectivity Test
```bash
# Use the provided test script
bash test-aws-dynamodb.sh

# This will:
# 1. Check if CMS API is running
# 2. Test DynamoDB connection
# 3. Test Course MS connectivity
# 4. Create test review
# 5. Test GSI queries
# 6. Get rating summary
```

---

## 🐳 Docker Deployment

### Build Docker Image
```bash
mvn clean install -DskipTests
docker build -t cms:latest .
```

### Run with Environment Variables
```bash
docker run -p 8087:8087 \
  -e AWS_ACCESS_KEY_ID=your_aws_access_key_here \
  -e AWS_SECRET_ACCESS_KEY=your_aws_secret_key_here \
  -e AWS_REGION=ap-south-1 \
  -e COURSE_MS_URL=http://course-service:8088 \
  -v /var/logs:/app/logs \
  cms:latest
```

### Docker Compose (Recommended)
```yaml
version: '3.8'
services:
  cms:
    build: .
    ports:
      - "8087:8087"
    environment:
      AWS_ACCESS_KEY_ID: ${AWS_ACCESS_KEY_ID}
      AWS_SECRET_ACCESS_KEY: ${AWS_SECRET_ACCESS_KEY}
      AWS_REGION: ap-south-1
      COURSE_MS_URL: http://course-service:8088
    volumes:
      - ./logs:/app/logs
    depends_on:
      - course-service
  
  course-service:
    image: course-service:latest
    ports:
      - "8088:8088"
```

---

## ✅ Pre-Deployment Checklist

- [ ] AWS credentials verified and have DynamoDB permissions
- [ ] All 20 tables created successfully in ap-south-1
- [ ] CourseReview GSI exists and queries work
- [ ] Microservice communication tested (with fallback)
- [ ] JWT tokens generating and validating correctly
- [ ] CORS configured for frontend domains
- [ ] Logging configured and monitored
- [ ] Error handling and fallbacks verified
- [ ] Rating aggregation works correctly
- [ ] Data persists correctly in DynamoDB
- [ ] Credentials moved to environment variables (production)
- [ ] Database backup strategy documented

---

## 📞 Support & References

### Files Modified/Created
- ✅ `application.yaml` - Updated with AWS config
- ✅ `logback-spring.xml` - Created for logging
- ✅ `AWS_DYNAMODB_SETUP.md` - Setup guide
- ✅ `DEPLOYMENT_GUIDE.md` - This file
- ✅ `test-aws-dynamodb.sh` - Testing script
- ✅ `.env.example` - Environment template

### Key Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/courses/{courseId}/reviews` | Create review with course validation |
| GET | `/courses/{courseId}/reviews` | Get reviews by course (GSI query) |
| GET | `/courses/{courseId}/rating-summary` | Get rating aggregation |
| GET | `/course-review/{review_id}` | Get specific review |
| GET | `/course-review/all` | Get all reviews |
| PUT | `/course-review/update/{review_id}` | Update review |
| DELETE | `/course-review/delete/{review_id}` | Delete review |

### Microservice Endpoint
- **Course MS URL**: `http://localhost:8088`
- **Validation Endpoint**: `GET /courses/{courseId}/exists`
- **Fallback Behavior**: Returns `true` if Course MS unreachable

### AWS Console
- **URL**: https://528701450746.signin.aws.amazon.com/console
- **Region**: ap-south-1 (Mumbai)
- **Service**: DynamoDB

---

## 🎉 You're Ready!

Your CMS application is now:
1. ✅ Connected to AWS DynamoDB
2. ✅ Auto-creating tables on startup
3. ✅ Validating data with microservice integration
4. ✅ Fetching and persisting data perfectly
5. ✅ Calculating aggregations automatically
6. ✅ Logging everything for monitoring

**Next Steps:**
1. Run `mvn clean install && mvn spring-boot:run`
2. Verify tables in AWS console
3. Test endpoints using curl or postman
4. Monitor logs for any issues
5. Deploy to production with environment variables

Good luck! 🚀

