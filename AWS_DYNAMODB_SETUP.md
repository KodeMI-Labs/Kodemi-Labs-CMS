# AWS DynamoDB Setup & Verification Guide

## ✅ Current Configuration Status

Your CMS application is now configured to connect to **AWS DynamoDB** with the following settings:

### AWS Credentials
- **Access Key**: `your_aws_access_key_here`
- **Secret Key**: `your_aws_secret_key_here`
- **Region**: `ap-south-1` (Mumbai, India)

### DynamoDB Configuration
- **Mode**: AWS (not local)
- **Use Local**: `false`
- **Endpoint**: AWS managed (ignored when use-local is false)

---

## 🚀 Quick Start

### 1. Build the Application
```bash
cd /Users/venkatkarthik/Downloads/CMS/CMS
mvn clean install
```

### 2. Run the Application
```bash
mvn spring-boot:run
```

The application will:
- Start on port **8087**
- Automatically create all DynamoDB tables in AWS (ap-south-1)
- Enable JWT authentication
- Enable Feign client for microservice communication

### 3. Verify Connection
Check the logs for messages like:
```
INFO - Created DynamoDB table: Banner
INFO - Created DynamoDB table: Blog
... (all 20 tables)
INFO - DynamoDB table already exists: CourseReview
INFO - CourseReview GSI 'course-id-index' is present.
```

---

## 📊 Tables Created Automatically

| Table Name | Hash Key | Purpose |
|---|---|---|
| Banner | banner_Id | Banner management |
| Blog | blog_Id | Blog content |
| FAQ | faq_Id | FAQ management |
| Media | meta_Id | Media assets |
| Notes | note_id | Study notes |
| SEO | seo_id | SEO configurations |
| StaticPages | page_id | Static page content |
| Testimonial | testimonial_id | Testimonials |
| Quiz | quiz_id | Quiz definitions |
| QuizCategory | category_id | Quiz categories |
| QuizQuestion | question_id | Quiz questions |
| QuizOption | option_id | Quiz answer options |
| QuizAttempt | attempt_id | User quiz attempts |
| QuizAnswer | answer_id | Quiz responses |
| QuizStudentResult | result_id | Student results |
| QuizAnalytics | analytics_id | Quiz analytics |
| CourseReview | review_id | Course reviews (with GSI: course-id-index) |
| CourseRatingSummary | course_id | Rating aggregations |
| InstructorReview | review_id | Instructor reviews |
| InstructorRatingSummary | instructor_id | Instructor ratings |

---

## 🔗 Microservice Integration

### CourseServiceClient Configuration
The CMS connects to the Course Microservice for validation:

- **Service URL**: `http://localhost:8088` (configured in `application.yaml`)
- **Endpoint**: `GET /courses/{courseId}/exists`
- **Purpose**: Validates course existence before creating reviews

### Feign Client with Circuit Breaker
- **Enabled**: Yes
- **Fallback**: `CourseServiceClientFallback`
- **Fallback Behavior**: Gracefully allows reviews if Course MS is unreachable
- **To disable graceful degradation**: Change `return true` to `return false` in `CourseServiceClientFallback`

---

## 📡 Data Fetching & Operations

### Example: Creating a Course Review
```bash
curl -X POST http://localhost:8087/courses/{courseId}/reviews \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "learner_id": "learner-123",
    "learner_name": "John Doe",
    "learner_profile_image": "https://...",
    "rating": 5,
    "comment": "Great course!"
  }'
```

**Flow**:
1. Controller receives request
2. Service validates course exists in Course MS
3. If valid → saves review to DynamoDB
4. Auto-updates CourseRatingSummary with new rating
5. Returns review response with ID

### Example: Fetching Reviews by Course
```bash
curl -X GET http://localhost:8087/courses/{courseId}/reviews \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Flow**:
1. Controller receives request
2. Service queries DynamoDB using GSI: `course-id-index`
3. Returns all reviews for that course (efficient query)

### Example: Getting Rating Summary
```bash
curl -X GET http://localhost:8087/courses/{courseId}/rating-summary \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response**:
```json
{
  "course_id": "course-123",
  "total_reviews": 10,
  "average_rating": 4.5,
  "five_star_count": 7,
  "four_star_count": 2,
  "three_star_count": 1,
  "two_star_count": 0,
  "one_star_count": 0
}
```

---

## 🔍 Verification Checklist

### ✓ Check DynamoDB Tables in AWS Console
1. Go to: https://console.aws.amazon.com/dynamodbv2/
2. Login with your credentials
3. Select region: **ap-south-1**
4. Verify all 20 tables exist
5. For CourseReview table:
   - Check Indexes tab
   - Verify GSI named `course-id-index` exists
   - GSI hash key should be `course_id`

### ✓ Test API Endpoints
```bash
# Get all course reviews
curl http://localhost:8087/course-review/all

# Get reviews by course (uses efficient GSI query)
curl http://localhost:8087/courses/{courseId}/reviews

# Get rating summary
curl http://localhost:8087/courses/{courseId}/rating-summary
```

### ✓ Check Application Logs
- Log file: `logs/cms.log`
- Console output should show DynamoDB table creation/existence
- Feign client logs show Course MS communication attempts
- Look for any connection errors or fallback invocations

### ✓ Test Data Persistence
1. Create a new review via API
2. Refresh the get reviews endpoint
3. Verify the review persists in DynamoDB
4. Check that CourseRatingSummary is updated

---

## 🛡️ Security Notes

### AWS Credentials
⚠️ **WARNING**: AWS credentials are currently in `application.yaml`

**For Production**:
- Move credentials to environment variables:
  ```bash
  export AWS_ACCESS_KEY_ID=your_aws_access_key_here
  export AWS_SECRET_ACCESS_KEY=your_aws_secret_key_here
  export AWS_REGION=ap-south-1
  ```
- Use AWS IAM roles (if running on EC2/ECS)
- Use AWS Secrets Manager
- Add `application.yaml` to `.gitignore`

### JWT Configuration
- JWT secret is configured in `application.yaml`
- Tokens expire based on `exp` claim
- Current token in config is for testing only

---

## 🔧 Troubleshooting

### Issue: "Could not connect to DynamoDB"
**Solution**:
1. Verify AWS credentials are correct
2. Check network connectivity to AWS
3. Verify IAM user has DynamoDB permissions
4. Check region is correct (ap-south-1)

### Issue: "Table already exists" warnings
**Solution**: This is normal. The initializer checks before creating.

### Issue: Course MS not reachable
**Solution**: Feign fallback will allow reviews to proceed. Check:
1. Course MS is running on port 8088
2. Update `course-ms.url` in `application.yaml` if different
3. Check network connectivity between services

### Issue: GSI not created for CourseReview
**Solution**: 
1. Delete CourseReview table in AWS console
2. Restart application (will recreate with GSI)
3. Or manually add GSI via AWS console

---

## 📈 Monitoring & Logging

### Log Levels
- **Application**: DEBUG (detailed logs for troubleshooting)
- **AWS DynamoDB**: INFO (connection and operation logs)
- **Feign Client**: DEBUG (microservice communication logs)

### Log File Location
- File: `logs/cms.log`
- Console: Enabled (check terminal/IDE output)

### Key Log Messages to Monitor
```
✅ "Created DynamoDB table: [TableName]"
✅ "DynamoDB table already exists: [TableName]"
✅ "Course MS is unreachable. Falling back..."
❌ "Failed to create table [TableName]"
❌ "Could not connect to DynamoDB"
```

---

## 🚢 Deployment

### Local Testing
```bash
mvn clean install
mvn spring-boot:run
```

### Docker (Optional)
Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk
COPY target/CMS-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
mvn clean install -DskipTests
docker build -t cms:latest .
docker run -p 8087:8087 \
  -e AWS_ACCESS_KEY_ID=your_aws_access_key_here \
  -e AWS_SECRET_ACCESS_KEY=your_aws_secret_key_here \
  -e AWS_REGION=ap-south-1 \
  cms:latest
```

---

## 📝 Configuration Files

### Main Configuration
**File**: `src/main/resources/application.yaml`
- AWS credentials
- DynamoDB mode (AWS vs Local)
- Microservice URLs
- JWT configuration
- CORS settings

### DynamoDB Config
**File**: `src/main/java/.../config/DynamoDbConfig.java`
- Handles AWS vs Local mode switching
- Creates DynamoDBMapper bean
- Sets up credentials and region

### Table Initializer
**File**: `src/main/java/.../config/DynamoDbTableInitializer.java`
- Auto-creates 20 tables on startup
- Handles special case for CourseReview with GSI
- Gracefully handles existing tables

### Logging Config
**File**: `src/main/resources/logback-spring.xml`
- Console and file logging
- Debug level for application code
- Info level for AWS and Spring Boot

---

## ✅ Final Checklist Before Going Live

- [ ] AWS credentials verified and IAM user has DynamoDB permissions
- [ ] All 20 tables created in ap-south-1
- [ ] CourseReview GSI present and working
- [ ] API endpoints tested and returning data
- [ ] Course MS microservice connectivity verified
- [ ] JWT token generation and validation working
- [ ] CORS configured for frontend domains
- [ ] Logging configured and monitored
- [ ] Error handling and fallbacks working
- [ ] Credentials moved to environment variables (if production)
- [ ] Database backup strategy in place

---

## 📞 Quick Reference

| Item | Value |
|---|---|
| Application Port | 8087 |
| AWS Region | ap-south-1 |
| DynamoDB Mode | AWS (not local) |
| Microservice Port | 8088 |
| Log File | logs/cms.log |
| JWT Config | application.yaml |

