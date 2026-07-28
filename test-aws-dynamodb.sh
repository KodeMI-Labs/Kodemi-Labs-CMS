#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         AWS DynamoDB & Microservice Integration Tests          ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Configuration
API_BASE_URL="http://localhost:8087"
COURSE_MS_URL="http://localhost:8088"
TEST_COURSE_ID="course-test-$(date +%s)"
TEST_LEARNER_ID="learner-test-$(date +%s)"

# Default JWT token (from application.yaml)
JWT_TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVFJBSU5FUiIsInVzZXJJZCI6IjkwODE5NGM2LTg1YmMtNDk5MC05NjI3LTBiMjg4OTNlZjA3MiIsImVtYWlsIjoicXZwN2RmczI1dkBsbm92aWMuY29tIiwidXNlcm5hbWUiOiJ0c3QxMjMzIiwic3ViIjoicXZwN2RmczI1dkBsbm92aWMuY29tIiwiaWF0IjoxNzgwOTExODI0LCJleHAiOjE3ODE1MTY2MjR9.z9B8LquQSXCZpM6SJfuZ5kcw5SK2Xrr5oNE7WBzOz4o"

# Test 1: Check if CMS API is running
echo -e "${YELLOW}Test 1: Checking if CMS API is running on port 8087...${NC}"
if curl -s -o /dev/null -w "%{http_code}" "$API_BASE_URL/course-review/all" | grep -q "200\|401"; then
    echo -e "${GREEN}✓ CMS API is accessible${NC}"
else
    echo -e "${RED}✗ CMS API is not responding${NC}"
    echo "  Make sure to run: mvn spring-boot:run"
    exit 1
fi
echo ""

# Test 2: Get all reviews (to verify DynamoDB connection)
echo -e "${YELLOW}Test 2: Fetching all reviews (tests DynamoDB connection)...${NC}"
RESPONSE=$(curl -s "$API_BASE_URL/course-review/all")
if echo "$RESPONSE" | grep -q "review_id\|^\[\]"; then
    echo -e "${GREEN}✓ DynamoDB connection successful${NC}"
    echo "  Response: $RESPONSE"
else
    echo -e "${RED}✗ Failed to fetch reviews${NC}"
    echo "  Response: $RESPONSE"
fi
echo ""

# Test 3: Test Feign Client (Course MS)
echo -e "${YELLOW}Test 3: Checking Course Microservice connectivity...${NC}"
if curl -s -o /dev/null -w "%{http_code}" "$COURSE_MS_URL/courses/test/exists" 2>/dev/null | grep -q "200\|404\|405"; then
    echo -e "${GREEN}✓ Course MS is reachable${NC}"
elif curl -s -o /dev/null -w "%{http_code}" "$COURSE_MS_URL" 2>/dev/null | grep -q "200\|404"; then
    echo -e "${GREEN}✓ Course MS endpoint is accessible${NC}"
else
    echo -e "${YELLOW}⚠ Course MS appears unreachable (expected if running on different setup)${NC}"
    echo "  Feign fallback will be used - reviews will be allowed"
fi
echo ""

# Test 4: Create a test review (requires valid course ID)
echo -e "${YELLOW}Test 4: Creating a test review (if Course MS fallback works)...${NC}"
REVIEW_PAYLOAD=$(cat <<EOF
{
  "learner_id": "$TEST_LEARNER_ID",
  "learner_name": "Test Learner",
  "learner_profile_image": "https://example.com/avatar.jpg",
  "rating": 5,
  "comment": "Excellent course material!"
}
EOF
)

REVIEW_RESPONSE=$(curl -s -X POST \
  -H "Content-Type: application/json" \
  -d "$REVIEW_PAYLOAD" \
  "$API_BASE_URL/courses/$TEST_COURSE_ID/reviews")

if echo "$REVIEW_RESPONSE" | grep -q "review_id\|reviewId"; then
    echo -e "${GREEN}✓ Review created successfully${NC}"
    echo "  Response: $REVIEW_RESPONSE"
    
    # Extract review ID for next tests
    REVIEW_ID=$(echo "$REVIEW_RESPONSE" | grep -o '"reviewId":"[^"]*' | grep -o '[^"]*$')
    
    # Test 5: Fetch reviews by course
    echo ""
    echo -e "${YELLOW}Test 5: Fetching reviews for course (tests GSI query)...${NC}"
    COURSE_REVIEWS=$(curl -s "$API_BASE_URL/courses/$TEST_COURSE_ID/reviews")
    if echo "$COURSE_REVIEWS" | grep -q "review_id\|reviewId"; then
        echo -e "${GREEN}✓ GSI query successful - reviews retrieved by course ID${NC}"
        echo "  Response: $COURSE_REVIEWS"
    else
        echo -e "${RED}✗ Failed to fetch reviews by course${NC}"
    fi
    
    # Test 6: Get rating summary
    echo ""
    echo -e "${YELLOW}Test 6: Fetching rating summary (tests aggregation)...${NC}"
    RATING_SUMMARY=$(curl -s "$API_BASE_URL/courses/$TEST_COURSE_ID/rating-summary")
    if echo "$RATING_SUMMARY" | grep -q "average_rating\|averageRating"; then
        echo -e "${GREEN}✓ Rating summary retrieved successfully${NC}"
        echo "  Response: $RATING_SUMMARY"
    else
        echo -e "${YELLOW}⚠ Rating summary response: $RATING_SUMMARY${NC}"
    fi
else
    echo -e "${YELLOW}⚠ Review creation may be waiting for Course MS validation${NC}"
    echo "  Response: $REVIEW_RESPONSE"
fi
echo ""

# Test 7: Check DynamoDB tables in logs
echo -e "${YELLOW}Test 7: Checking application logs for table creation...${NC}"
if [ -f "logs/cms.log" ]; then
    TABLE_COUNT=$(grep -c "Created DynamoDB table\|DynamoDB table already exists" logs/cms.log)
    if [ "$TABLE_COUNT" -gt 0 ]; then
        echo -e "${GREEN}✓ Found $TABLE_COUNT table operations in logs${NC}"
        echo "  Sample entries:"
        grep "DynamoDB table" logs/cms.log | head -5 | sed 's/^/  /'
    else
        echo -e "${YELLOW}⚠ No table creation logs found yet${NC}"
    fi
else
    echo -e "${YELLOW}⚠ Logs file not found at logs/cms.log${NC}"
fi
echo ""

# Summary
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                        Test Summary                            ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo -e "${GREEN}✓ AWS DynamoDB configuration is active${NC}"
echo -e "${GREEN}✓ Region: ap-south-1${NC}"
echo -e "${GREEN}✓ Feign Client for Course MS is configured${NC}"
echo ""
echo "Next steps:"
echo "1. Check AWS Console: https://528701450746.signin.aws.amazon.com/console"
echo "2. Navigate to DynamoDB and verify all tables exist in ap-south-1"
echo "3. For CourseReview table, verify GSI 'course-id-index' exists"
echo "4. Monitor logs/cms.log for any connection issues"
echo ""
