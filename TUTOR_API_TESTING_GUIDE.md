# LMS Tutor API - Complete Test Guide

## 📋 Quick Start

### Cách 1: Import Postman Collection
1. Mở Postman
2. Click **Import** → **File** 
3. Chọn `TUTOR_API_TEST_COLLECTION.json`
4. Tất cả endpoints sẽ có sẵn

### Cách 2: Run cURL Commands
```bash
chmod +x tutor_api_test.sh
./tutor_api_test.sh
```

### Cách 3: Manual cURL (Copy-paste)
Xem chi tiết ở phần **Manual Test Commands** bên dưới

---

## 🎯 API Endpoints Overview

| Group | Method | Endpoint | Description |
|-------|--------|----------|-------------|
| **Course** | GET | `/api/tutors/courses/{id}` | Get course details |
| **Course** | PUT | `/api/tutors/courses/{id}` | Update course |
| **Course** | DELETE | `/api/tutors/courses/{id}` | Delete course |
| **Mentee** | GET | `/api/tutors/courses/{id}/mentees` | Get mentees in course |
| **Exercise** | POST | `/api/tutors/exercises` | Create exercise |
| **Exercise** | DELETE | `/api/tutors/exercises/{id}` | Delete exercise |
| **Submission** | GET | `/api/tutors/exercises/{id}/submissions` | Get submissions |
| **Submission** | PUT | `/api/tutors/submissions/{id}/grade` | Grade submission |
| **Rating** | GET | `/api/tutors/sessions/{id}/ratings` | Get ratings |
| **Rating** | PUT | `/api/tutors/ratings/{id}/reply` | Reply to rating |
| **Rating** | POST | `/api/tutors/ratings/{id}/report` | Report rating |
| **Message** | POST | `/api/tutors/conversations/join` | Join conversation |
| **Message** | POST | `/api/tutors/conversations/{id}/messages` | Send message |
| **Announcement** | GET | `/api/tutors/announcements` | Get announcements |
| **Announcement** | GET | `/api/tutors/announcements/admin/{id}` | Get by admin |
| **Announcement** | DELETE | `/api/tutors/announcements/{id}` | Delete announcement |

---

## 📝 Manual Test Commands

### 1️⃣ COURSE MANAGEMENT

#### Get Course By ID
```bash
curl -X GET http://localhost:8080/api/tutors/courses/2 \
  -H 'Content-Type: application/json'
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Course retrieved successfully",
  "data": {
    "courseId": 2,
    "title": "Advanced Java Programming",
    "description": "Learn Java from basics to advanced",
    "level": "Intermediate",
    "maxStudents": 30,
    "currentEnrollment": 25
  }
}
```

#### Update Course
```bash
curl -X PUT http://localhost:8080/api/tutors/courses/2 \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Advanced Java - Updated",
    "description": "Updated description",
    "level": "Advanced",
    "maxStudents": 35
  }'
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Course updated successfully",
  "data": { ... }
}
```

#### Delete Course
```bash
curl -X DELETE http://localhost:8080/api/tutors/courses/2
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Course deleted successfully"
}
```

---

### 2️⃣ MENTEE & EXERCISE MANAGEMENT

#### Get All Mentees In Course
```bash
curl -X GET 'http://localhost:8080/api/tutors/courses/2/mentees?page=0&size=10'
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Mentees retrieved successfully",
  "pagination": {
    "content": [
      {
        "enrollmentId": 1,
        "menteeId": 2,
        "menteeName": "Nguyen Van A",
        "menteeEmail": "a.nguyen@example.com",
        "grade": 8.5,
        "status": "ACTIVE"
      }
    ],
    "totalElements": 25,
    "totalPages": 3,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

#### Create Exercise
```bash
curl -X POST http://localhost:8080/api/tutors/exercises \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Exercise 1: Java Basics",
    "description": "Write a Java program to calculate factorial",
    "courseId": 2,
    "dueDate": "2025-02-15",
    "maxScore": 100
  }'
```

**Expected Response (201):**
```json
{
  "statusCode": 201,
  "message": "Exercise created successfully",
  "data": {
    "exerciseId": 5,
    "title": "Exercise 1: Java Basics",
    "courseId": 2,
    "maxScore": 100
  }
}
```

#### Delete Exercise
```bash
curl -X DELETE http://localhost:8080/api/tutors/exercises/5
```

---

### 3️⃣ SUBMISSION MANAGEMENT

#### Get All Submissions
```bash
curl -X GET 'http://localhost:8080/api/tutors/exercises/5/submissions?page=0&size=10'
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Submissions retrieved successfully",
  "pagination": {
    "content": [
      {
        "submissionId": 1,
        "exerciseId": 5,
        "menteeId": 2,
        "menteeName": "Nguyen Van A",
        "submissionDate": "2025-02-14",
        "grade": 8.5,
        "status": "GRADED"
      }
    ],
    "totalElements": 25,
    "totalPages": 3,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

#### Grade Submission
```bash
curl -X PUT 'http://localhost:8080/api/tutors/submissions/1/grade?grade=8.5'
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Submission graded successfully",
  "data": {
    "submissionId": 1,
    "grade": 8.5,
    "status": "GRADED"
  }
}
```

---

### 4️⃣ RATING MANAGEMENT

#### Get All Ratings
```bash
curl -X GET 'http://localhost:8080/api/tutors/sessions/1/ratings?page=0&size=10'
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Ratings retrieved successfully",
  "pagination": {
    "content": [
      {
        "ratingId": 1,
        "sessionId": 1,
        "menteeId": 2,
        "menteeName": "Nguyen Van A",
        "score": 5,
        "comment": "Excellent tutor!",
        "ratedAt": "2025-02-28"
      }
    ],
    "totalElements": 2,
    "totalPages": 1,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

#### Reply To Rating
```bash
curl -X PUT 'http://localhost:8080/api/tutors/ratings/1/reply?reply=Thank%20you%20for%20your%20feedback!'
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Reply added successfully",
  "data": {
    "ratingId": 1,
    "reply": "Thank you for your feedback!"
  }
}
```

#### Report Rating
```bash
curl -X POST http://localhost:8080/api/tutors/ratings/2/report \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Inappropriate Comment",
    "description": "This rating contains inappropriate language"
  }'
```

**Expected Response (201):**
```json
{
  "statusCode": 201,
  "message": "Rating reported successfully",
  "data": {
    "reportId": 1,
    "ratingId": 2,
    "title": "Inappropriate Comment",
    "status": "PENDING"
  }
}
```

---

### 5️⃣ CONVERSATION & MESSAGE

#### Join Conversation
```bash
curl -X POST 'http://localhost:8080/api/tutors/conversations/join?menteeId=2&tutorId=7'
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Joined conversation successfully",
  "data": {
    "conversationId": 1,
    "menteeId": 2,
    "tutorId": 7,
    "createdAt": "2025-11-30T10:00:00"
  }
}
```

#### Send Message
```bash
curl -X POST http://localhost:8080/api/tutors/conversations/1/messages \
  -H 'Content-Type: application/json' \
  -d '{
    "senderId": 7,
    "content": "Hi, do you have any questions about the exercise?"
  }'
```

**Expected Response (201):**
```json
{
  "statusCode": 201,
  "message": "Message sent successfully",
  "data": {
    "messageId": 1,
    "conversationId": 1,
    "senderId": 7,
    "senderName": "Java Tutor",
    "content": "Hi, do you have any questions about the exercise?",
    "sentAt": "2025-11-30T10:15:00"
  }
}
```

---

### 6️⃣ ANNOUNCEMENT MANAGEMENT

#### Get All Announcements
```bash
curl -X GET 'http://localhost:8080/api/tutors/announcements?page=0&size=10&recipientType=TUTORS'
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Announcements retrieved successfully",
  "pagination": {
    "content": [
      {
        "announcementId": 1,
        "title": "Course Schedule Update",
        "content": "The course schedule has been updated...",
        "recipientType": "TUTORS",
        "adminName": "Admin User",
        "createdAt": "2025-11-20T08:00:00",
        "isRead": true
      }
    ],
    "totalElements": 2,
    "totalPages": 1,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

#### Get Announcements By Admin
```bash
curl -X GET 'http://localhost:8080/api/tutors/announcements/admin/1?page=0&size=10'
```

#### Delete Announcement
```bash
curl -X DELETE http://localhost:8080/api/tutors/announcements/1
```

**Expected Response (200):**
```json
{
  "statusCode": 200,
  "message": "Announcement deleted successfully"
}
```

---

## 🧪 Testing Scenarios

### Scenario 1: Complete Course Management Flow
```bash
# 1. Get course details
curl -X GET http://localhost:8080/api/tutors/courses/2

# 2. View mentees enrolled
curl -X GET 'http://localhost:8080/api/tutors/courses/2/mentees?page=0&size=10'

# 3. Create exercise for course
curl -X POST http://localhost:8080/api/tutors/exercises \
  -H 'Content-Type: application/json' \
  -d '{"title": "Ex 1", "courseId": 2, "dueDate": "2025-02-15", "maxScore": 100}'

# 4. Check submissions
curl -X GET 'http://localhost:8080/api/tutors/exercises/{exerciseId}/submissions?page=0&size=10'

# 5. Grade a submission
curl -X PUT 'http://localhost:8080/api/tutors/submissions/{submissionId}/grade?grade=8.5'
```

### Scenario 2: Student Interaction Flow
```bash
# 1. Join conversation with mentee
curl -X POST 'http://localhost:8080/api/tutors/conversations/join?menteeId=2&tutorId=7'

# 2. Send message
curl -X POST http://localhost:8080/api/tutors/conversations/{conversationId}/messages \
  -H 'Content-Type: application/json' \
  -d '{"senderId": 7, "content": "How are you doing with the course?"}'

# 3. Check ratings received
curl -X GET 'http://localhost:8080/api/tutors/sessions/{sessionId}/ratings?page=0&size=10'

# 4. Reply to rating
curl -X PUT 'http://localhost:8080/api/tutors/ratings/{ratingId}/reply?reply=Thank%20you!'
```

---

## ⚠️ Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| 404 Not Found | Verify courseId, exerciseId, etc. exist in database |
| 400 Bad Request | Check JSON format and required fields |
| 403 Forbidden | Tutor may not have access to this resource |
| 500 Server Error | Check server logs for details |

---

## 📊 Data Requirements

### Required IDs for Testing
- **courseId**: 2 (must exist)
- **menteeId**: 2 (must exist)
- **tutorId**: 7 (must exist)
- **sessionId**: 1 (must exist)
- **exerciseId**: 5 (or create first)
- **submissionId**: 1 (or create first)
- **ratingId**: 1 (or create first)

### Sample Data Setup
If IDs don't exist, you can check what exists:
```bash
# Check database for valid IDs
mysql -u root -p lms -e "SELECT id FROM courses LIMIT 5;"
mysql -u root -p lms -e "SELECT id FROM mentees LIMIT 5;"
mysql -u root -p lms -e "SELECT id FROM tutors LIMIT 5;"
```

---

## 🚀 Next Steps

1. **Test each endpoint individually** using cURL
2. **Test complete flows** (e.g., create → grade → review)
3. **Check error handling** (try invalid IDs, malformed JSON, etc.)
4. **Verify pagination** (test with different page/size values)
5. **Monitor logs** for any issues

---

**Created:** Nov 30, 2025
**API Version:** 1.0
**Base URL:** `http://localhost:8080/api/tutors`
