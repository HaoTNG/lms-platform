# Security Configuration - Implementation Summary

## ✅ Đã Hoàn Thành

### 1. **Dependencies Added (pom.xml)**
- ✅ JJWT 0.12.3 (JWT token generation and validation)
  - jjwt-api
  - jjwt-impl (runtime)
  - jjwt-jackson (runtime)

### 2. **Security Classes Created**

#### 2.1 JwtUtil.java (`/security/JwtUtil.java`)
- ✅ Tạo JWT token: `generateToken(userId, email, role)`
- ✅ Lấy userId từ token: `getUserIdFromToken(token)`
- ✅ Lấy email từ token: `getEmailFromToken(token)`
- ✅ Lấy role từ token: `getRoleFromToken(token)`
- ✅ Validate token: `validateToken(token)`
- ✅ Check token hết hạn: `isTokenExpired(token)`

#### 2.2 JwtUserDetails.java (`/security/JwtUserDetails.java`)
- ✅ DTO chứa thông tin user từ JWT
- ✅ Fields: userId, email, role

#### 2.3 JwtAuthenticationFilter.java (`/security/JwtAuthenticationFilter.java`)
- ✅ Filter xử lý JWT từ cookie
- ✅ Extract token từ cookie "Authorization"
- ✅ Set SecurityContext với authorities dựa trên role
- ✅ Support 3 roles: ADMIN, TUTOR, MENTEE

### 3. **Configuration**

#### 3.1 SecurityConfig.java (Updated)
- ✅ JWT authentication filter được add trước UsernamePasswordAuthenticationFilter
- ✅ Public endpoints: `/api/auth/**`, `/api/public/**`, Swagger
- ✅ Admin endpoints: `/api/admin/**` (require ROLE_ADMIN)
- ✅ Tutor endpoints: `/api/tutor/**` (require ROLE_TUTOR)
- ✅ Mentee endpoints: `/api/mentee/**` (require ROLE_MENTEE)
- ✅ User endpoints: `/api/user/**` (require authenticated)
- ✅ CORS config cho localhost:3000 và localhost:5173
- ✅ BCryptPasswordEncoder bean

#### 3.2 application.properties (Updated)
- ✅ JWT secret key
- ✅ JWT expiration time (24 hours)
- ✅ Server port (8080)

### 4. **Auth Controller** (`/controller/AuthController.java`)
- ✅ POST `/api/auth/register` - Đăng ký tài khoản mới
  - Check email đã tồn tại
  - Encode password với BCrypt
  - Tạo JWT token
  - Set token vào cookie (HttpOnly, SameSite)
  
- ✅ POST `/api/auth/login` - Đăng nhập
  - Validate email/password
  - Tạo JWT token
  - Set token vào cookie
  
- ✅ POST `/api/auth/logout` - Đăng xuất
  - Xóa cookie Authorization
  
- ✅ GET `/api/auth/me` - Kiểm tra authentication status

### 5. **Updated DTOs**
- ✅ UserDTO: Thêm `password_hashed` field

---

## 🎯 API Endpoints

### Public (Không cần xác thực)
```
POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/logout
GET    /api/auth/me
GET    /swagger-ui/**
GET    /v3/api-docs/**
```

### Admin Only
```
GET    /api/admin/**
POST   /api/admin/**
PUT    /api/admin/**
DELETE /api/admin/**
```

### Tutor Only
```
GET    /api/tutor/**
POST   /api/tutor/**
PUT    /api/tutor/**
DELETE /api/tutor/**
```

### Mentee Only
```
GET    /api/mentee/**
POST   /api/mentee/**
PUT    /api/mentee/**
DELETE /api/mentee/**
```

### Any Authenticated User
```
GET    /api/user/**
POST   /api/user/**
```

---

## 🔒 Security Features

1. **JWT Token Storage**: Cookie (HttpOnly = true)
2. **Token Validation**: HS512 algorithm
3. **Password Hashing**: BCrypt (10 rounds)
4. **Role-Based Access Control**: ADMIN, TUTOR, MENTEE
5. **CORS Protection**: Configured origins
6. **CSRF Protection**: Disabled (vì dùng cookie + token)

---

## 📝 Example Workflow

### 1. User Register
```
POST /api/auth/register
Body: { name, email, password_hashed, role }
Response: 200 + Cookie: Authorization=<jwt_token>
```

### 2. User Access Admin Resource
```
GET /api/admin/manage-user
Cookie: Authorization=<jwt_token>
Response: 200 (if role=ADMIN) or 403 (if not)
```

### 3. User Logout
```
POST /api/auth/logout
Cookie: Authorization=<jwt_token>
Response: 200 + Cookie: Authorization= (cleared)
```

---

## 🧪 Testing

### Curl Example - Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@example.com",
    "password_hashed": "pass123",
    "role": "ADMIN"
  }' -c cookies.txt
```

### Curl Example - Access Admin API
```bash
curl -X GET http://localhost:8080/api/admin/manage-user \
  -b cookies.txt
```

---

## ⚙️ Configuration Options

### application.properties
```properties
# JWT Secret (change in production!)
app.jwt.secret=mySecretKeyThatIsVeryLongAndSecureForJWTSigningPurpose123456789

# JWT Expiration (milliseconds) - 24 hours
app.jwt.expiration=86400000

# Server port
server.port=8080
```

---

## 📋 File Structure

```
src/main/java/com/example/lms/
├── security/
│   ├── JwtUtil.java                    ✅ JWT token operations
│   ├── JwtUserDetails.java             ✅ User info from JWT
│   └── JwtAuthenticationFilter.java    ✅ Cookie to JWT filter
├── config/
│   └── SecurityConfig.java             ✅ Security configuration
├── controller/
│   └── AuthController.java             ✅ Register/Login/Logout
└── dto/
    └── UserDTO.java                    ✅ Updated with password

src/main/resources/
└── application.properties              ✅ JWT config
```

---

## ✨ Next Steps (Optional)

- [ ] Implement Refresh Token mechanism
- [ ] Add Rate Limiting on auth endpoints
- [ ] Implement Email Verification
- [ ] Add Remember-me functionality
- [ ] Implement Multi-Factor Authentication (MFA)
- [ ] Add audit logging for security events

---

## 🚀 Production Deployment

1. Update `app.jwt.secret` with strong random key
2. Change `cookie.setSecure(true)` for HTTPS
3. Update CORS origins
4. Change `ddl-auto` to `validate`
5. Disable Swagger/OpenAPI
6. Enable HTTPS/TLS
7. Set appropriate cookie SameSite policy
