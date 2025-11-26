# API Documentation - LMS Platform

## Mục Lục
1. [Authentication API](#authentication-api)
2. [Admin API](#admin-api)
3. [Cookie & Security](#cookie--security)
4. [Response Format](#response-format)

---

## Authentication API

### Base URL: `/api/auth`

### 1. **Register - Đăng ký tài khoản**

```
POST /api/auth/register
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "MENTEE"  // Optional: MENTEE (default), TUTOR, or ADMIN
}
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "User registered successfully",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "MENTEE"
  }
}
```

**Set-Cookie Header:**
```
Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...; 
HttpOnly; 
Secure; 
SameSite=Strict; 
Path=/; 
Max-Age=86400
```

**Error Responses:**
- `400 Bad Request` - Email already exists
- `500 Server Error` - Unexpected error

---

### 2. **Login - Đăng nhập**

```
POST /api/auth/login
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Login successful",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "MENTEE"
  }
}
```

**Set-Cookie Header:**
```
Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...; 
HttpOnly; 
Secure; 
SameSite=Strict; 
Path=/; 
Max-Age=86400
```

**Error Response (401 Unauthorized):**
```json
{
  "statusCode": 401,
  "message": "Invalid email or password"
}
```

---

### 3. **Logout - Đăng xuất**

```
POST /api/auth/logout
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Logout successful"
}
```

**Set-Cookie Header (expires immediately):**
```
Authorization=; 
HttpOnly; 
Secure; 
SameSite=Strict; 
Path=/; 
Max-Age=0
```

---

### 4. **Check Authentication - Kiểm tra trạng thái đăng nhập**

```
GET /api/auth/me
Authorization: [Cookie tự động gửi]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Authenticated",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "MENTEE"
  }
}
```

**Error Response (401 Unauthorized):**
```json
{
  "statusCode": 401,
  "message": "Unauthorized"
}
```

---

## Admin API

### Base URL: `/api/admin`

> **Yêu cầu:** Người dùng phải có role `ADMIN` để truy cập các endpoint này

---

### **USER MANAGEMENT**

#### 1. **Get Users - Lấy danh sách người dùng**

```
GET /api/admin/manage-user?page=0&size=10&search=&role=MENTEE
Authorization: [Cookie]
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | int | No | Trang (default: 0) |
| size | int | No | Số lượng per page (default: 10) |
| search | string | No | Tìm kiếm theo tên/email |
| role | string | No | Filter: MENTEE, TUTOR, ADMIN |

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Users retrieved successfully",
  "data": [
    {
      "id": 2,
      "name": "Jane Doe",
      "email": "jane@example.com",
      "role": "MENTEE"
    }
  ]
}
```

---

#### 2. **Create User - Tạo người dùng mới**

```
POST /api/admin/create-user
Content-Type: application/json
Authorization: [Cookie]
```

**Request Body:**
```json
{
  "name": "New User",
  "email": "newuser@example.com",
  "password": "password123",
  "role": "TUTOR"
}
```

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "User created successfully",
  "user": {
    "id": 3,
    "name": "New User",
    "email": "newuser@example.com",
    "role": "TUTOR"
  }
}
```

---

#### 3. **Get User by ID - Lấy thông tin người dùng cụ thể**

```
GET /api/admin/get-user/1
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "User retrieved successfully",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "MENTEE"
  }
}
```

---

### **COURSE MANAGEMENT**

#### 1. **Get All Courses - Lấy danh sách khóa học**

```
GET /api/admin/courses?page=0&size=10&tutor=&status=&course_name=
Authorization: [Cookie]
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| page | int | Trang (default: 0) |
| size | int | Số lượng per page (default: 10) |
| tutor | string | Tìm kiếm theo giáo viên |
| status | string | Filter: OPEN, END |
| course_name | string | Tìm kiếm theo tên khóa học |

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Courses retrieved successfully",
  "data": [
    {
      "courseId": 1,
      "courseName": "Java 101",
      "description": "Introduction to Java",
      "maxMentee": 50,
      "courseStatus": "OPEN",
      "startDate": "2024-01-01",
      "endDate": "2024-12-31"
    }
  ]
}
```

---

#### 2. **Create Course - Tạo khóa học mới**

```
POST /api/admin/courses
Content-Type: application/json
Authorization: [Cookie]
```

**Request Body:**
```json
{
  "courseName": "Python Advanced",
  "description": "Advanced Python Programming",
  "maxMentee": 30,
  "courseStatus": "OPEN",
  "startDate": "2024-02-01",
  "endDate": "2024-08-31",
  "subjectRegistration": {
    "id": 1
  }
}
```

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "Course created successfully",
  "data": {
    "courseId": 2,
    "courseName": "Python Advanced",
    "courseStatus": "OPEN"
  }
}
```

---

### **REPORT TICKET MANAGEMENT**

#### 1. **Create Report Ticket - Tạo báo cáo sự cố**

```
POST /api/admin/report-tickets
Content-Type: application/json
Authorization: [Cookie]
```

**Request Body:**
```json
{
  "title": "Bug in submission system",
  "description": "Students cannot submit assignments",
  "ticketStatus": "OPEN"
}
```

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "Report ticket created successfully",
  "data": {
    "id": 1,
    "title": "Bug in submission system",
    "ticketStatus": "OPEN"
  }
}
```

---

#### 2. **Get All Report Tickets - Lấy danh sách báo cáo**

```
GET /api/admin/report-tickets?page=0&size=10
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Report tickets retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Bug in submission system",
      "ticketStatus": "OPEN"
    }
  ]
}
```

---

#### 3. **Get Report Ticket by Status - Lấy báo cáo theo trạng thái**

```
GET /api/admin/report-tickets/status/OPEN
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Report tickets retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Bug in submission system",
      "ticketStatus": "OPEN"
    }
  ]
}
```

---

#### 4. **Update Report Ticket Status - Cập nhật trạng thái báo cáo**

```
PUT /api/admin/report-tickets/1?status=RESOLVED&adminResponse=Fixed
Authorization: [Cookie]
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | string | Yes | OPEN, RESOLVED, REJECTED |
| adminResponse | string | No | Phản hồi từ admin |

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Report ticket updated successfully",
  "data": {
    "id": 1,
    "ticketStatus": "RESOLVED",
    "adminResponse": "Fixed"
  }
}
```

---

#### 5. **Delete Report Ticket - Xóa báo cáo**

```
DELETE /api/admin/report-tickets/1
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Report ticket deleted successfully"
}
```

---

### **ANNOUNCEMENT MANAGEMENT**

#### 1. **Send Announcement to All - Gửi thông báo cho tất cả**

```
POST /api/admin/announcements/send-all
Content-Type: application/json
Authorization: [Cookie]
```

**Request Body:**
```json
{
  "title": "Important Update",
  "content": "System maintenance scheduled for tonight",
  "recipientType": "ALL"
}
```

> **Note:** `senderId` sẽ được trích xuất tự động từ JWT token

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "Announcement sent to all users successfully",
  "data": {
    "id": 1,
    "title": "Important Update",
    "content": "System maintenance scheduled for tonight",
    "recipientType": "ALL",
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

---

#### 2. **Send Announcement to Mentee - Gửi thông báo cho sinh viên**

```
POST /api/admin/announcements/send-mentee
Content-Type: application/json
Authorization: [Cookie]
```

**Request Body:**
```json
{
  "title": "Assignment Deadline",
  "content": "Assignment deadline is extended to tomorrow",
  "recipientType": "MENTEE"
}
```

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "Announcement sent to all mentees successfully",
  "data": {
    "id": 2,
    "title": "Assignment Deadline",
    "recipientType": "MENTEE"
  }
}
```

---

#### 3. **Send Announcement to Tutor - Gửi thông báo cho giáo viên**

```
POST /api/admin/announcements/send-tutor
Content-Type: application/json
Authorization: [Cookie]
```

**Request Body:**
```json
{
  "title": "Grade Review",
  "content": "Please review grades for this semester",
  "recipientType": "TUTOR"
}
```

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "Announcement sent to all tutors successfully",
  "data": {
    "id": 3,
    "title": "Grade Review",
    "recipientType": "TUTOR"
  }
}
```

---

#### 4. **Send Announcement to Specific User - Gửi thông báo cho 1 người**

```
POST /api/admin/announcements/send-user/5
Content-Type: application/json
Authorization: [Cookie]
```

**Request Body:**
```json
{
  "title": "Personal Message",
  "content": "Your account needs verification",
  "recipientUserId": 5
}
```

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "Announcement sent to user successfully",
  "data": {
    "id": 4,
    "title": "Personal Message",
    "recipientUserId": 5
  }
}
```

---

#### 5. **Get All Announcements - Lấy danh sách thông báo**

```
GET /api/admin/announcements?page=0&size=10&recipientType=ALL&title=&adminId=
Authorization: [Cookie]
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| page | int | Trang (default: 0) |
| size | int | Số lượng per page (default: 10) |
| recipientType | string | Filter: ALL, MENTEE, TUTOR, USER |
| title | string | Tìm kiếm theo tiêu đề |
| adminId | long | Filter theo admin ID |

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Announcements retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Important Update",
      "content": "System maintenance scheduled for tonight",
      "recipientType": "ALL",
      "createdAt": "2024-01-15T10:30:00"
    }
  ]
}
```

---

#### 6. **Get Announcements by Admin - Lấy thông báo của admin cụ thể**

```
GET /api/admin/announcements/admin/1?page=0&size=10
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Announcements retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Important Update",
      "adminId": 1
    }
  ]
}
```

---

#### 7. **Delete Announcement - Xóa thông báo**

```
DELETE /api/admin/announcements/1
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Announcement deleted successfully"
}
```

---

### **ANALYTICS MANAGEMENT**

#### 1. **Get All Analytics - Lấy tất cả thống kê**

```
GET /api/admin/analytics
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "All analytics retrieved successfully",
  "data": {
    "systemStats": {
      "totalUsers": 100,
      "totalAdmins": 2,
      "totalTutors": 10,
      "totalMentees": 88,
      "totalCourses": 15,
      "activeCourses": 10,
      "upcomingCourses": 0,
      "finishedCourses": 5,
      "totalEnrollments": 250
    },
    "studentAnalytics": {
      "performanceDistribution": {
        "excellentCount": 30,
        "goodCount": 35,
        "averageCount": 20,
        "weakCount": 3,
        "averageScoreOverall": 7.8
      },
      "submissionAnalysis": {
        "onTimeSubmissions": 180,
        "lateSubmissions": 40,
        "completionRate": 88.0
      }
    },
    "tutorAnalytics": {
      "tutorPerformances": {
        "John Tutor": {
          "tutorId": 5,
          "tutorName": "John Tutor",
          "averageRating": 4.5,
          "totalCourses": 3,
          "completedCourses": 1,
          "courseCompletionRate": 33.33,
          "studentDistribution": {
            "excellentStudents": 10,
            "goodStudents": 15,
            "averageStudents": 5,
            "weakStudents": 0,
            "averageStudentScore": 7.9
          }
        }
      }
    }
  }
}
```

---

#### 2. **Get System Statistics - Lấy thống kê hệ thống**

```
GET /api/admin/analytics/system
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "System statistics retrieved successfully",
  "data": {
    "totalUsers": 100,
    "totalAdmins": 2,
    "totalTutors": 10,
    "totalMentees": 88,
    "totalCourses": 15,
    "activeCourses": 10,
    "upcomingCourses": 0,
    "finishedCourses": 5,
    "totalEnrollments": 250
  }
}
```

---

#### 3. **Get Student Analytics - Lấy thống kê sinh viên**

```
GET /api/admin/analytics/students
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Student analytics retrieved successfully",
  "data": {
    "performanceDistribution": {
      "excellentCount": 30,
      "goodCount": 35,
      "averageCount": 20,
      "weakCount": 3,
      "averageScoreOverall": 7.8
    },
    "submissionAnalysis": {
      "onTimeSubmissions": 180,
      "lateSubmissions": 40,
      "completionRate": 88.0
    }
  }
}
```

---

#### 4. **Get Tutor Analytics - Lấy thống kê giáo viên**

```
GET /api/admin/analytics/tutors
Authorization: [Cookie]
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Tutor analytics retrieved successfully",
  "data": {
    "tutorPerformances": {
      "John Tutor": {
        "tutorId": 5,
        "tutorName": "John Tutor",
        "averageRating": 4.5,
        "totalCourses": 3,
        "completedCourses": 1,
        "courseCompletionRate": 33.33,
        "studentDistribution": {
          "excellentStudents": 10,
          "goodStudents": 15,
          "averageStudents": 5,
          "weakStudents": 0,
          "averageStudentScore": 7.9
        }
      }
    }
  }
}
```

---

## Cookie & Security

### Cookie Cấu hình

```
Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...; 
HttpOnly;           // Không thể truy cập qua JavaScript (chặn XSS)
Secure;             // Chỉ gửi qua HTTPS (set true ở production)
SameSite=Strict;    // Chặn CSRF, không gửi khi cross-site
Path=/;             // Có hiệu lực toàn bộ website
Max-Age=86400       // Hết hạn sau 24 giờ (86400 giây)
```

### JWT Token Structure

Token được lưu trong cookie `Authorization` với format:

```
Header.Payload.Signature
```

**Payload chứa:**
```json
{
  "userId": "1",
  "email": "john@example.com",
  "role": "MENTEE",
  "iat": 1673865600,
  "exp": 1673952000
}
```

---

## Response Format

### Success Response (200 OK)

```json
{
  "statusCode": 200,
  "message": "Operation successful",
  "user": null,        // Hoặc user object nếu có
  "data": null         // Hoặc data object nếu có
}
```

### Error Response (4xx/5xx)

```json
{
  "statusCode": 400,
  "message": "Error message explaining what went wrong"
}
```

### Common Status Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Success |
| 201 | Created - Resource created |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Authentication failed |
| 403 | Forbidden - No permission |
| 404 | Not Found - Resource not found |
| 500 | Server Error |

---

## Security Best Practices

1. **Cookie tự động gửi**: Browser tự động gửi cookie `Authorization` trong mọi request
2. **HttpOnly**: Cookie không thể truy cập từ JavaScript (bảo vệ XSS)
3. **CORS**: Frontend phải được cấu hình để gửi cookies cross-origin
4. **Token Validation**: Mỗi request được kiểm tra JWT token từ cookie
5. **Role-based Access**: Admin endpoints yêu cầu role ADMIN

---

## JavaScript/Frontend Examples

### Fetch với Cookie (Automatically)

```javascript
// Cookie tự động gửi khi same-origin
fetch('https://api.example.com/api/auth/me', {
  method: 'GET',
  credentials: 'include'  // Quan trọng! Gửi cookies
})
.then(res => res.json())
.then(data => console.log(data));
```

### Cross-Origin Request

```javascript
fetch('https://api.example.com/api/admin/manage-user', {
  method: 'GET',
  credentials: 'include'  // Gửi cookies cross-origin
})
.then(res => res.json())
.then(data => console.log(data));
```

### Register & Auto-Login

```javascript
// 1. Register
const registerRes = await fetch('/api/auth/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    name: 'John',
    email: 'john@example.com',
    password: 'pass123',
    role: 'MENTEE'
  })
});

// Cookie tự động được set ở đây!
// 2. Kiểm tra authentication (cookie tự động gửi)
const meRes = await fetch('/api/auth/me', {
  credentials: 'include'
});
const userData = await meRes.json();
console.log(userData); // Có user info
```

### Logout

```javascript
// Clear cookie
await fetch('/api/auth/logout', {
  method: 'POST',
  credentials: 'include'
});
// Cookie tự động expired

// Hoặc redirect home
window.location.href = '/login';
```

---

## Notes

- Tất cả timestamps theo UTC
- Pagination mặc định từ 0
- Default page size = 10
- SearchBox có thể search theo tên hoặc email
- Analytics data được cập nhật real-time từ database
