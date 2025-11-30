#!/bin/bash

# LMS Tutor API - cURL Test Commands
# ====================================

BASE_URL="http://localhost:8080/api/tutors"

echo "==================== TUTOR API TEST COMMANDS ===================="

# ==================== COURSE MANAGEMENT ====================
echo ""
echo "### COURSE MANAGEMENT ###"

echo ""
echo "1. Get Course By ID"
echo "curl -X GET \"$BASE_URL/courses/2\" \\"
echo "  -H 'Content-Type: application/json'"
curl -X GET "$BASE_URL/courses/2" \
  -H 'Content-Type: application/json' | jq '.' 2>/dev/null || echo "Failed to fetch course"

echo ""
echo "2. Update Course"
echo "curl -X PUT \"$BASE_URL/courses/2\" \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{...}'"
curl -X PUT "$BASE_URL/courses/2" \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Advanced Java Programming - Updated",
    "description": "Updated course description",
    "syllabus": "Updated syllabus content",
    "level": "Advanced",
    "maxStudents": 35
  }' | jq '.' 2>/dev/null || echo "Failed to update course"

echo ""
echo "3. Delete Course"
echo "curl -X DELETE \"$BASE_URL/courses/2\""
curl -X DELETE "$BASE_URL/courses/2" | jq '.' 2>/dev/null || echo "Failed to delete course"

# ==================== MENTEE & EXERCISE MANAGEMENT ====================
echo ""
echo ""
echo "### MENTEE & EXERCISE MANAGEMENT ###"

echo ""
echo "4. Get All Mentees In Course"
echo "curl -X GET \"$BASE_URL/courses/2/mentees?page=0&size=10\""
curl -X GET "$BASE_URL/courses/2/mentees?page=0&size=10" | jq '.' 2>/dev/null || echo "Failed to fetch mentees"

echo ""
echo "5. Create Exercise"
echo "curl -X POST \"$BASE_URL/exercises\" \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{...}'"
curl -X POST "$BASE_URL/exercises" \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Exercise 1: Java Basics",
    "description": "Write a Java program to calculate factorial",
    "courseId": 2,
    "dueDate": "2025-02-15",
    "maxScore": 100
  }' | jq '.' 2>/dev/null || echo "Failed to create exercise"

echo ""
echo "6. Delete Exercise"
echo "curl -X DELETE \"$BASE_URL/exercises/5\""
curl -X DELETE "$BASE_URL/exercises/5" | jq '.' 2>/dev/null || echo "Failed to delete exercise"

# ==================== SUBMISSION MANAGEMENT ====================
echo ""
echo ""
echo "### SUBMISSION MANAGEMENT ###"

echo ""
echo "7. Get All Submissions"
echo "curl -X GET \"$BASE_URL/exercises/5/submissions?page=0&size=10\""
curl -X GET "$BASE_URL/exercises/5/submissions?page=0&size=10" | jq '.' 2>/dev/null || echo "Failed to fetch submissions"

echo ""
echo "8. Grade Submission"
echo "curl -X PUT \"$BASE_URL/submissions/1/grade?grade=8.5\""
curl -X PUT "$BASE_URL/submissions/1/grade?grade=8.5" | jq '.' 2>/dev/null || echo "Failed to grade submission"

# ==================== RATING MANAGEMENT ====================
echo ""
echo ""
echo "### RATING MANAGEMENT ###"

echo ""
echo "9. Get All Ratings"
echo "curl -X GET \"$BASE_URL/sessions/1/ratings?page=0&size=10\""
curl -X GET "$BASE_URL/sessions/1/ratings?page=0&size=10" | jq '.' 2>/dev/null || echo "Failed to fetch ratings"

echo ""
echo "10. Reply To Rating"
echo "curl -X PUT \"$BASE_URL/ratings/1/reply?reply=Thank%20you%20for%20your%20feedback!\""
curl -X PUT "$BASE_URL/ratings/1/reply?reply=Thank%20you%20for%20your%20feedback!" | jq '.' 2>/dev/null || echo "Failed to reply to rating"

echo ""
echo "11. Report Rating"
echo "curl -X POST \"$BASE_URL/ratings/2/report\" \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{...}'"
curl -X POST "$BASE_URL/ratings/2/report" \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Inappropriate Comment",
    "description": "This rating contains inappropriate language"
  }' | jq '.' 2>/dev/null || echo "Failed to report rating"

# ==================== CONVERSATION & MESSAGE ====================
echo ""
echo ""
echo "### CONVERSATION & MESSAGE ###"

echo ""
echo "12. Join Conversation"
echo "curl -X POST \"$BASE_URL/conversations/join?menteeId=2&tutorId=7\""
curl -X POST "$BASE_URL/conversations/join?menteeId=2&tutorId=7" | jq '.' 2>/dev/null || echo "Failed to join conversation"

echo ""
echo "13. Send Message"
echo "curl -X POST \"$BASE_URL/conversations/1/messages\" \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{...}'"
curl -X POST "$BASE_URL/conversations/1/messages" \
  -H 'Content-Type: application/json' \
  -d '{
    "senderId": 7,
    "content": "Hi, do you have any questions about the exercise?"
  }' | jq '.' 2>/dev/null || echo "Failed to send message"

# ==================== ANNOUNCEMENT MANAGEMENT ====================
echo ""
echo ""
echo "### ANNOUNCEMENT MANAGEMENT ###"

echo ""
echo "14. Get All Announcements"
echo "curl -X GET \"$BASE_URL/announcements?page=0&size=10&recipientType=TUTORS\""
curl -X GET "$BASE_URL/announcements?page=0&size=10&recipientType=TUTORS" | jq '.' 2>/dev/null || echo "Failed to fetch announcements"

echo ""
echo "15. Get Announcements By Admin"
echo "curl -X GET \"$BASE_URL/announcements/admin/1?page=0&size=10\""
curl -X GET "$BASE_URL/announcements/admin/1?page=0&size=10" | jq '.' 2>/dev/null || echo "Failed to fetch admin announcements"

echo ""
echo "16. Delete Announcement"
echo "curl -X DELETE \"$BASE_URL/announcements/1\""
curl -X DELETE "$BASE_URL/announcements/1" | jq '.' 2>/dev/null || echo "Failed to delete announcement"

echo ""
echo "==================== TEST COMPLETE ===================="
