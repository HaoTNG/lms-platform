# LMS Platform - Learning Management System

Một hệ thống quản lý học tập (LMS) được xây dựng bằng **Spring Boot 3** với kiến trúc service-driven, hỗ trợ các vai trò: Admin, Tutor, và Mentee.

---

## 📋 Mục Lục

1. [Tổng Quan Hệ Thống](#tổng-quan-hệ-thống)
2. [Kiến Trúc Ứng Dụng](#kiến-trúc-ứng-dụng)
3. [Công Nghệ Sử Dụng](#công-nghệ-sử-dụng)
4. [Cài Đặt & Khởi Chạy](#cài-đặt--khởi-chạy)
5. [Cấu Hình](#cấu-hình)
6. [Cơ Sở Dữ Liệu](#cơ-sở-dữ-liệu)
7. [API Documentation](#api-documentation)
8. [Cấu Trúc Thư Mục](#cấu-trúc-thư-mục)
9. [Các Entity Chính](#các-entity-chính)
10. [Các Controllers](#các-controllers)
11. [Xác Thực & Phân Quyền](#xác-thực--phân-quyền)
12. [Phương Pháp Phát Triển](#phương-pháp-phát-triển)

---

## 🎯 Tổng Quan Hệ Thống

**LMS Platform** là một nền tảng học tập trực tuyến toàn diện cho phép:

- **Admin**: Quản lý người dùng, khóa học, giáo viên, và phân tích dữ liệu
- **Tutor**: Tạo/quản lý khóa học, bài tập, chấm điểm, quản lý học viên, gửi thông báo
- **Mentee**: Đăng ký khóa học, nộp bài tập, xem điểm, trò chuyện, để lại đánh giá

### 🎓 Những Tính Năng Chính

| Tính Năng | Mô Tả |
|-----------|-------|
| **Xác thực & Phân quyền** | JWT-based authentication, role-based access control (RBAC) |
| **Quản lý Khóa Học** | CRUD operations, enrollment management, course status tracking |
| **Bài Tập & Nộp Bài** | Create exercises, submit solutions, auto-grading support |
| **Hệ Thống Đánh Giá** | Học viên đánh giá giáo viên, giáo viên trả lời review |
| **Trò Chuyện** | Real-time messaging giữa tutor và mentee |
| **Thông Báo** | Gửi thông báo tới Admin/Tutor/Mentee |
| **Báo Cáo & Phân Tích** | Thống kê học viên, tiến độ khóa học, lịch sử hoạt động |

---

## 🏗️ Kiến Trúc Ứng Dụng

### Mô Hình 3-Layer

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← REST API endpoints
├─────────────────────────────────────┤
│         Service Layer               │  ← Business logic
├─────────────────────────────────────┤
│       Repository Layer (JPA)        │  ← Database access
└─────────────────────────────────────┘
```

### Design Patterns

- **DTO Pattern**: Tách entity từ API response
- **Mapper Pattern**: Convert entity ↔ DTO
- **Repository Pattern**: Abstraction for database operations
- **Service Pattern**: Centralized business logic
- **Factory Pattern**: `UserFactory` để tạo user objects

---

## 🛠️ Công Nghệ Sử Dụng

### Backend Framework
- **Spring Boot**: 3.3.4
- **Java**: 21
- **Spring Security**: JWT authentication
- **Spring Data JPA**: Database ORM

### Database
- **PostgreSQL**: Production database
- **H2**: Embedded database for testing

### Libraries
- **Lombok**: Giảm boilerplate code
- **MapStruct**: DTO mapping (thay thế Jackson)
- **Jackson**: JSON serialization
- **JJWT**: JWT token generation
- **SpringDoc OpenAPI**: Swagger/OpenAPI documentation

### Build Tool
- **Maven**: 3.x

---

## 🚀 Cài Đặt & Khởi Chạy

### Yêu Cầu Tiên Quyết
- Java 21 JDK
- Maven 3.6+
- PostgreSQL 12+ (hoặc sử dụng H2 cho development)
- Git

### Bước 1: Clone Repository
```bash
git clone https://github.com/HaoTNG/lms-platform.git
cd lms-platform
```

### Bước 2: Cấu Hình Database

#### Dùng PostgreSQL (Recommended)
```bash
# Tạo database
createdb lms

# Tạo user (hoặc sử dụng user hiện tại)
createuser lms -P
# Nhập password: lms123

# Cấp quyền
psql -U postgres -d lms -c "GRANT ALL PRIVILEGES ON DATABASE lms TO lms;"
```

#### Dùng H2 (Development Only)
Bỏ comment dòng này trong `application.properties`:
```properties
#spring.datasource.url=jdbc:postgresql://localhost:5432/lms
spring.datasource.url=jdbc:h2:mem:testdb
```

### Bước 3: Cài Đặt Dependencies
```bash
mvn clean install
```

### Bước 4: Khởi Chạy Application
```bash
mvn spring-boot:run
```

Hoặc build JAR và chạy:
```bash
mvn clean package
java -jar target/lms-1.0.0.jar
```

### Bước 5: Kiểm Tra Status
- **Server**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

---

## ⚙️ Cấu Hình

### application.properties

```properties
# ==================== Database ====================
spring.datasource.url=jdbc:postgresql://localhost:5432/lms
spring.datasource.username=lms
spring.datasource.password=lms123
spring.jpa.hibernate.ddl-auto=update  # auto, create, update, validate, none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# ==================== JWT ====================
app.jwt.secret=mySecretKeyThatIsVeryLongAndSecureForJWTSigningPurpose123456789LONGERFORBESTSTABILITY
app.jwt.expiration=86400000  # 24 hours in milliseconds

# ==================== Server ====================
server.port=8080

# ==================== Logging ====================
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Bảo Mật JWT

**⚠️ PRODUCTION**: Thay đổi `app.jwt.secret` thành một random key dài:
```bash
# Generate secure key
openssl rand -base64 32
```

---

## 🗄️ Cơ Sở Dữ Liệu

### ER Diagram (Simplified)

```
User (parent)
├── Admin
├── Tutor
└── Mentee

Course
├── Lesson
│   ├── Exercise
│   │   └── Submission
│   └── Resource
├── Enrollment (Mentee ↔ Course)
├── SubjectRegistration (Tutor ↔ Subject)
├── Session (Tutor-Mentee interaction)
│   └── Rating (Mentee → Tutor feedback)
├── Announcement
└── Message
    └── Conversation
```

### Các Bảng Chính

| Bảng | Mô Tả |
|------|-------|
| `users` | Người dùng cơ bản (base entity) |
| `admin` | Nhân viên quản lý |
| `tutor` | Giáo viên |
| `mentee` | Học viên |
| `course` | Khóa học |
| `lesson` | Bài học |
| `exercise` | Bài tập |
| `submission` | Nộp bài của học viên |
| `enrollment` | Đăng ký khóa học |
| `session` | Phiên học |
| `rating` | Đánh giá của học viên |
| `conversation` | Cuộc trò chuyện |
| `message` | Tin nhắn |
| `announcement` | Thông báo |
| `resource` | Tài liệu học tập |

---

## 📡 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Headers
```bash
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

### Các Endpoint Chính

#### 🔐 Authentication (`/auth`)
| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| POST | `/auth/register` | Đăng ký tài khoản (User, Mentee, Tutor) |
| POST | `/auth/login` | Đăng nhập, lấy JWT token |
| POST | `/auth/logout` | Đăng xuất |
| GET | `/auth/me` | Lấy thông tin user hiện tại |

#### 👨‍💼 Admin (`/admin`)
| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| GET | `/admin/users` | Danh sách tất cả người dùng |
| GET | `/admin/users/{id}` | Chi tiết người dùng |
| PUT | `/admin/users/{id}` | Cập nhật người dùng |
| DELETE | `/admin/users/{id}` | Xóa người dùng |
| GET | `/admin/courses` | Danh sách khóa học |
| GET | `/admin/analytics` | Thống kê hệ thống |

#### 👨‍🏫 Tutor (`/tutor`)
| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| GET | `/tutor/courses/{id}` | Chi tiết khóa học |
| PUT | `/tutor/courses/{id}` | Cập nhật khóa học |
| DELETE | `/tutor/courses/{id}` | Xóa khóa học |
| GET | `/tutor/courses/{id}/mentees` | Danh sách học viên (phân trang) |
| POST | `/tutor/exercises` | Tạo bài tập |
| DELETE | `/tutor/exercises/{id}` | Xóa bài tập |
| GET | `/tutor/exercises/{id}/submissions` | Xem nộp bài (phân trang) |
| PUT | `/tutor/submissions/{id}/grade` | Chấm điểm |
| GET | `/tutor/sessions/{id}/ratings` | Xem đánh giá (phân trang) |
| PUT | `/tutor/ratings/{id}/reply` | Trả lời đánh giá |
| POST | `/tutor/conversations/join` | Tham gia cuộc trò chuyện |
| POST | `/tutor/conversations/{id}/messages` | Gửi tin nhắn |
| GET | `/tutor/announcements` | Xem thông báo (phân trang) |
| DELETE | `/tutor/announcements/{id}` | Xóa thông báo |

#### 👨‍🎓 Mentee (`/mentee`)
| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| GET | `/mentee/{id}` | Chi tiết học viên |
| POST | `/mentee/enroll` | Đăng ký khóa học |
| POST | `/mentee/unenroll` | Hủy đăng ký khóa học |
| GET | `/mentee/mycourses` | Danh sách khóa học của tôi |
| GET | `/mentee/courses` | Danh sách khóa học có sẵn |
| GET | `/mentee/courses/{courseId}` | Chi tiết khóa học |
| GET | `/mentee/course/{courseId}/lessons` | Danh sách bài học |
| GET | `/mentee/lesson/{lessonId}` | Chi tiết bài học |
| GET | `/mentee/lesson/{lessonId}/resources` | Tài liệu bài học |
| GET | `/mentee/lesson/{lessonId}/exercises` | Bài tập bài học |
| POST | `/mentee/exercise/{exerciseId}/submit` | Nộp bài |
| GET | `/mentee/exercise/{exerciseId}` | Chi tiết bài tập |
| GET | `/mentee/exercise/{exerciseId}/submissions` | Xem nộp bài của tôi |

### Pagination Format

```json
{
  "statusCode": 200,
  "message": "Success",
  "pagination": {
    "content": [...],
    "totalElements": 100,
    "totalPages": 10,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

### Response Format

**Success (200, 201):**
```json
{
  "statusCode": 200,
  "message": "Success message",
  "data": { ... }
}
```

**Error (400, 403, 404, 500):**
```json
{
  "statusCode": 400,
  "message": "Error message"
}
```

---

## 📁 Cấu Trúc Thư Mục

```
src/main/java/com/example/lms/
├── LmsApplication.java                 # Entry point
├── config/
│   └── SecurityConfig.java             # Spring Security configuration
├── controller/
│   ├── AuthController.java             # Authentication endpoints
│   ├── AdminController.java            # Admin endpoints
│   ├── TutorController.java            # Tutor endpoints
│   ├── MenteeController.java           # Mentee endpoints
│   └── UserController.java             # User endpoints
├── service/
│   ├── interf/
│   │   ├── UserService.java
│   │   ├── AdminService.java
│   │   ├── TutorService.java
│   │   ├── MenteeService.java
│   │   └── AdminAnalyticsService.java
│   ├── imple/
│   │   ├── UserServiceImple.java
│   │   ├── AdminServiceImple.java
│   │   ├── TutorServiceImple.java
│   │   ├── MenteeServiceImple.java
│   │   └── AdminAnalyticsServiceImpl.java
│   └── UserFactory.java                # Factory for creating user objects
├── repository/
│   ├── UserRepository.java
│   ├── AdminRepository.java
│   ├── TutorRepository.java
│   ├── MenteeRepository.java
│   ├── CourseRepository.java
│   ├── LessonRepository.java
│   ├── ExerciseRepository.java
│   ├── SubmissionRepository.java
│   ├── EnrollmentRepository.java
│   ├── SessionRepository.java
│   ├── RatingRepository.java
│   ├── ConversationRepository.java
│   ├── MessageRepository.java
│   ├── AnnouncementRepository.java
│   ├── ResourceRepository.java
│   ├── ForumRepository.java
│   ├── QuestionRepository.java
│   └── SubjectRegistrationRepository.java
├── model/
│   ├── User.java                       # Base entity (inheritance)
│   ├── Admin.java
│   ├── Tutor.java
│   ├── Mentee.java
│   ├── Course.java
│   ├── Lesson.java
│   ├── Exercise.java
│   ├── Submission.java
│   ├── Enrollment.java
│   ├── Session.java
│   ├── Rating.java
│   ├── Conversation.java
│   ├── Message.java
│   ├── Announcement.java
│   ├── Resource.java
│   ├── Forum.java
│   ├── Question.java
│   ├── Subject.java
│   ├── SubjectRegistration.java
│   ├── AnnouncementUser.java
│   └── ReportTicket.java
├── dto/
│   ├── Response.java                   # API response wrapper
│   ├── UserDTO.java
│   ├── AdminDTO.java
│   ├── TutorDTO.java
│   ├── MenteeDTO.java
│   ├── CourseDTO.java
│   ├── LessonDTO.java
│   ├── ExerciseDTO.java
│   ├── SubmissionDTO.java
│   ├── SessionDTO.java
│   ├── RatingDTO.java
│   ├── ConversationDTO.java
│   ├── MessageDTO.java
│   ├── AnnouncementDTO.java
│   ├── ResourceDTO.java
│   ├── QuestionDTO.java
│   ├── ReportTicketDTO.java
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── CreateRatingRequest.java
│   ├── AskQuestionRequest.java
│   ├── SendMessageRequest.java
│   ├── EnrollCourseRequest.java
│   ├── SubjectRegistrationRequest.java
│   └── AdminAnalyticsDTO.java
├── mapper/
│   ├── UserMapper.java                 # Entity ↔ DTO conversion
│   ├── CourseMapper.java
│   ├── LessonMapper.java
│   ├── ExerciseMapper.java
│   ├── SubmissionMapper.java
│   ├── SessionMapper.java
│   ├── RatingMapper.java
│   ├── ConversationMapper.java
│   ├── MessageMapper.java
│   ├── AnnouncementMapper.java
│   ├── ResourceMapper.java
│   ├── QuestionMapper.java
│   ├── ReportTicketMapper.java
│   └── (more mappers as needed)
├── security/
│   ├── JwtUtil.java                    # JWT token generation/validation
│   ├── JwtAuthenticationFilter.java    # JWT authentication filter
│   └── JwtUserDetails.java             # User details for Spring Security
├── enums/
│   ├── UserRole.java                   # ADMIN, TUTOR, MENTEE
│   ├── CourseStatus.java               # DRAFT, PUBLISHED, ARCHIVED
│   ├── EnrollmentStatus.java           # ACTIVE, SUSPENDED, COMPLETED
│   ├── RegistrationStatus.java         # PENDING, APPROVED, REJECTED
│   ├── ResourceType.java               # PDF, VIDEO, DOCUMENT, LINK
│   ├── SessionType.java                # ONLINE, OFFLINE
│   ├── RecipientType.java              # ADMIN, TUTOR, MENTEE
│   ├── ReportTicketStatus.java         # PENDING, RESOLVED, REJECTED
│   └── (more enums)
└── src/main/resources/
    ├── application.properties           # Configuration file
    └── (other resources)
```

---

## 🗂️ Các Entity Chính

### 1. User (Base Entity)
```java
@Entity
public class User {
    Long id;
    String email;           // Unique
    String password;        // Hashed
    String firstName;
    String lastName;
    UserRole role;          // ADMIN, TUTOR, MENTEE
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
```

**Subclasses:**
- `Admin`: Người quản lý hệ thống
- `Tutor`: Giáo viên, tạo khóa học & quản lý học viên
- `Mentee`: Học viên, đăng ký khóa học & nộp bài

### 2. Course
```java
@Entity
public class Course {
    Long id;
    Tutor tutor;            // FK
    String title;
    String description;
    CourseStatus status;    // DRAFT, PUBLISHED, ARCHIVED
    Integer maxStudents;
    Integer currentEnrollment;
    LocalDateTime createdAt;
    List<Lesson> lessons;
    List<Enrollment> enrollments;
}
```

### 3. Lesson
```java
@Entity
public class Lesson {
    Long id;
    Course course;          // FK
    String title;
    String description;
    List<Exercise> exercises;
    List<Resource> resources;
}
```

### 4. Exercise
```java
@Entity
public class Exercise {
    Long id;
    Lesson lesson;          // FK
    String title;
    String description;
    Double maxScore;
    LocalDate dueDate;
    List<Submission> submissions;
}
```

### 5. Submission
```java
@Entity
public class Submission {
    Long id;
    Exercise exercise;      // FK
    Mentee mentee;          // FK
    String content;         // File URL or text
    Double grade;           // Submitted by tutor
    LocalDateTime submittedAt;
    LocalDateTime gradedAt;
}
```

### 6. Enrollment
```java
@Entity
public class Enrollment {
    Long id;
    Course course;          // FK
    Mentee mentee;          // FK
    EnrollmentStatus status;
    Double currentGrade;
    LocalDateTime enrolledAt;
}
```

### 7. Session
```java
@Entity
public class Session {
    Long id;
    Tutor tutor;            // FK
    Mentee mentee;          // FK
    SessionType type;       // ONLINE, OFFLINE
    LocalDateTime startTime;
    LocalDateTime endTime;
    String description;
    List<Rating> ratings;
}
```

### 8. Rating
```java
@Entity
public class Rating {
    Long id;
    Session session;        // FK
    Integer score;          // 1-5 stars
    String comment;
    String reply;           // Tutor's reply
    LocalDateTime createdAt;
}
```

### 9. Conversation
```java
@Entity
public class Conversation {
    Long id;
    Tutor tutor;            // FK
    Mentee mentee;          // FK
    LocalDateTime createdAt;
    List<Message> messages;
}
```

### 10. Message
```java
@Entity
public class Message {
    Long id;
    Conversation conversation; // FK
    User sender;            // FK (Tutor or Mentee)
    String content;
    LocalDateTime sentAt;
}
```

---

## 🎮 Các Controllers

### AuthController
- `POST /api/auth/register` - Đăng ký tài khoản
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/logout` - Đăng xuất
- `GET /api/auth/me` - Thông tin user hiện tại

### AdminController
- `GET /api/admin/users` - Danh sách người dùng
- `GET /api/admin/users/{id}` - Chi tiết người dùng
- `PUT /api/admin/users/{id}` - Cập nhật người dùng
- `DELETE /api/admin/users/{id}` - Xóa người dùng
- `GET /api/admin/courses` - Danh sách khóa học
- `GET /api/admin/analytics` - Thống kê

### TutorController
- Quản lý khóa học (CRUD)
- Quản lý bài tập & chấm điểm
- Quản lý học viên
- Gửi thông báo

### MenteeController
- Đăng ký/Hủy đăng ký khóa học
- Xem danh sách khóa học
- Nộp bài & xem điểm
- Xem bài học & tài liệu

### UserController
- Cập nhật thông tin cá nhân
- Thay đổi mật khẩu

---

## 🔐 Xác Thực & Phân Quyền

### JWT Authentication Flow

```
1. User Login
   ↓
2. Server validates credentials
   ↓
3. Generate JWT token
   ↓
4. Return token to client
   ↓
5. Client includes token in Authorization header
   ↓
6. JwtAuthenticationFilter validates token
   ↓
7. Request allowed if valid
```

### Role-Based Access Control (RBAC)

```java
// Security Config Rules
.requestMatchers("/api/admin/**").hasRole("ADMIN")
.requestMatchers("/api/tutor/**").hasRole("TUTOR")
.requestMatchers("/api/mentee/**").hasRole("MENTEE")
.requestMatchers("/api/auth/**").permitAll()
.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
```

### JWT Token Structure
```
Header:  { "alg": "HS256", "typ": "JWT" }
Payload: { "userId": 1, "email": "user@example.com", "role": "TUTOR" }
Signature: HMACSHA256(header.payload, secret)
```

---

## 📚 Phương Pháp Phát Triển

### Các Công Việc Phổ Biến

#### 1. Tạo API Endpoint Mới

**Bước 1: Tạo/Update DTO**
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewFeatureDTO {
    private Long id;
    private String title;
    // ... other fields
}
```

**Bước 2: Tạo/Update Mapper**
```java
@Mapper(componentModel = "spring")
public interface NewFeatureMapper {
    NewFeatureDTO toDTO(NewFeature entity);
    NewFeature toEntity(NewFeatureDTO dto);
}
```

**Bước 3: Update Service Interface**
```java
public interface NewFeatureService {
    NewFeatureDTO getById(Long id);
    List<NewFeatureDTO> getAll();
    NewFeatureDTO create(NewFeatureDTO dto);
    NewFeatureDTO update(Long id, NewFeatureDTO dto);
    void delete(Long id);
}
```

**Bước 4: Implement Service**
```java
@Service
@RequiredArgsConstructor
public class NewFeatureServiceImpl implements NewFeatureService {
    private final NewFeatureRepository repository;
    private final NewFeatureMapper mapper;
    
    @Override
    public NewFeatureDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Not found")));
    }
    // ... implement other methods
}
```

**Bước 5: Tạo Controller Endpoint**
```java
@RestController
@RequestMapping("/api/feature")
@RequiredArgsConstructor
public class NewFeatureController {
    private final NewFeatureService service;
    
    @GetMapping("/{id}")
    public ResponseEntity<Response<NewFeatureDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
            new Response<>(200, "Success", service.getById(id))
        );
    }
    // ... other endpoints
}
```

#### 2. Thêm Fields vào Entity

```java
@Entity
public class User {
    // ... existing fields
    
    @Column(nullable = false)
    private String newField;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

Hibernate sẽ tự động tạo/update column khi chạy (`ddl-auto=update`).

#### 3. Tạo Custom Query trong Repository

```java
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTutorId(Long tutorId);
    
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' ORDER BY c.createdAt DESC")
    List<Course> findPublishedCourses();
    
    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
```

#### 4. Thêm Custom Exception

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
```

Dùng trong service:
```java
throw new NotFoundException("Course not found with id: " + id);
```

---

## 🧪 Testing & Debugging

### Sử dụng Test Files

#### Postman Collection
```bash
# Import vào Postman
File → Import → TUTOR_API_TEST_COLLECTION.json
```

#### cURL Commands
```bash
chmod +x tutor_api_test.sh
./tutor_api_test.sh
```

#### Manual cURL Testing
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "tutor@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "TUTOR"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "tutor@example.com",
    "password": "password123"
  }'

# Use token
TOKEN=<jwt_token_from_login>
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN"
```

### Kiểm Tra Logs

```bash
# Xem SQL queries
tail -f target/logs/application.log | grep "SQL"

# Xem Hibernate bind parameters
tail -f target/logs/application.log | grep "BasicBinder"
```

---

## 🔧 Troubleshooting

| Vấn Đề | Giải Pháp |
|--------|----------|
| `Connection refused to localhost:5432` | Kiểm tra PostgreSQL service, hoặc dùng H2 |
| `401 Unauthorized` | JWT token invalid/expired, login lại |
| `403 Forbidden` | User không có role để access endpoint |
| `404 Not Found` | Resource ID không tồn tại |
| `org.hibernate.id.IdentifierGenerationException` | @GeneratedValue không được set |
| `LazyInitializationException` | Truy cập lazy-loaded collection ngoài transaction |

---

## 📦 Deployment

### Build JAR
```bash
mvn clean package -DskipTests
```

### Run JAR
```bash
java -jar target/lms-1.0.0.jar
```

### Docker (Optional)
```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/lms-1.0.0.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Build & Run:
```bash
docker build -t lms-platform .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/lms \
  -e SPRING_DATASOURCE_USERNAME=lms \
  -e SPRING_DATASOURCE_PASSWORD=lms123 \
  lms-platform
```

---

## 📝 Coding Standards

### Naming Conventions
- **Classes**: PascalCase (e.g., `UserController`)
- **Methods**: camelCase (e.g., `getUserById`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_PAGE_SIZE`)
- **Database tables**: snake_case (e.g., `user_profiles`)

### Code Organization
- 1 class per file
- Related classes grouped in same package
- Interfaces separate from implementations
- DTOs grouped in `dto` package
- Mappers grouped in `mapper` package

### Annotations Usage
```java
@Entity              // Mark as JPA entity
@Table(name="...")   // Specify table name
@Data                // Lombok: generate getters/setters
@RequiredArgsConstructor  // Lombok: constructor for final fields
@RestController      // Mark as Spring MVC controller
@Service             // Mark as business service
@Repository          // Mark as data access layer
@Transactional       // Mark for transaction management
@Validated           // Enable validation
@NotNull             // Validate non-null
@Email               // Validate email format
```

---

## 📖 Useful Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
- [JWT Introduction](https://tools.ietf.org/html/rfc7519)
- [MapStruct Docs](https://mapstruct.org/)
- [Lombok Features](https://projectlombok.org/features/all)

---

## 👥 Contributors

- **Lead Developer**: HaoTNG
- **Project**: LMS Platform

---

## 📄 License

This project is proprietary and intended for educational purposes only.

---

## 📞 Support

Để báo cáo bugs hoặc request features, vui lòng tạo GitHub Issue.

---

Frontend: https://github.com/HaoTNG/lms-frontend
**Last Updated**: February 25, 2026  
**Version**: 1.0.0  
**Status**: Active Development
