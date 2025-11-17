# LMS Platform - Security & JWT Configuration

## 📋 Tổng quan

Hệ thống sử dụng **JWT (JSON Web Token)** lưu trữ trong **Cookie** để xác thực và phân quyền người dùng.

### Phiên bản
- **Spring Boot**: 3.3.4
- **Java**: 21
- **JJWT**: 0.12.3

---

## 🔐 Cấu trúc Security

### 1. **Public Endpoints** (Không cần xác thực)
```
POST   /api/auth/register  - Đăng ký tài khoản
POST   /api/auth/login     - Đăng nhập
POST   /api/auth/logout    - Đăng xuất
GET    /api/auth/me        - Kiểm tra trạng thái (public nhưng phải authenticated)
```

### 2. **Admin Endpoints** (Yêu cầu ROLE_ADMIN)
```
GET    /api/admin/...
POST   /api/admin/...
PUT    /api/admin/...
DELETE /api/admin/...
```

### 3. **Tutor Endpoints** (Yêu cầu ROLE_TUTOR)
```
GET    /api/tutor/...
POST   /api/tutor/...
PUT    /api/tutor/...
DELETE /api/tutor/...
```

### 4. **Mentee Endpoints** (Yêu cầu ROLE_MENTEE)
```
GET    /api/mentee/...
POST   /api/mentee/...
PUT    /api/mentee/...
DELETE /api/mentee/...
```

---

## 📝 JSON Examples

### 1. Register (Đăng ký)

**Request:**
```json
POST /api/auth/register

{
  "name": "Nguyễn Văn A",
  "email": "nguyenvana@example.com",
  "password_hashed": "password123",
  "role": "MENTEE"
}
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "User registered successfully",
  "user": {
    "name": "Nguyễn Văn A",
    "email": "nguyenvana@example.com",
    "role": "MENTEE"
  }
}
```

### 2. Login (Đăng nhập)

**Request:**
```
POST /api/auth/login?email=nguyenvana@example.com&password=password123
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "Login successful",
  "user": {
    "name": "Nguyễn Văn A",
    "email": "nguyenvana@example.com",
    "role": "MENTEE"
  }
}
```

**Cookie được set:**
```
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJuZ3V5ZW52YW5hQGV4YW1wbGUuY29tIiwicm9sZSI6Ik1FTlRFRSIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.xxx
```

### 3. Logout (Đăng xuất)

**Request:**
```
POST /api/auth/logout
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "Logout successful"
}
```

### 4. Access Admin API

**Request:**
```
GET /api/admin/manage-user
```

**Cookie tự động được gửi:**
```
Cookie: Authorization=eyJhbGciOiJIUzUxMiJ9...
```

**Response (nếu có role ADMIN):**
```json
{
  "statusCode": 200,
  "message": "Success",
  "userList": [...]
}
```

**Response (nếu không có role ADMIN):**
```json
{
  "statusCode": 403,
  "message": "Access Denied"
}
```

---

## 🧪 Test với cURL

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyễn Văn A",
    "email": "nguyenvana@example.com",
    "password_hashed": "password123",
    "role": "ADMIN"
  }' \
  -c cookies.txt
```

### Login
```bash
curl -X POST "http://localhost:8080/api/auth/login?email=nguyenvana@example.com&password=password123" \
  -c cookies.txt
```

### Access Protected Resource (Admin)
```bash
curl -X GET http://localhost:8080/api/admin/manage-user \
  -b cookies.txt
```

### Logout
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -b cookies.txt
```

---

## ⚙️ Configuration trong application.properties

```properties
# JWT Secret key (dùng cho signing token)
app.jwt.secret=mySecretKeyThatIsVeryLongAndSecureForJWTSigningPurpose123456789LONGERFORBESTSTABILITY

# JWT Expiration time (milliseconds) - 24 hours
app.jwt.expiration=86400000
```

---

## 🔑 JWT Token Structure

Ví dụ JWT token được giải mã:
```json
{
  "header": {
    "alg": "HS512",
    "typ": "JWT"
  },
  "payload": {
    "sub": "1",
    "email": "nguyenvana@example.com",
    "role": "ADMIN",
    "iat": 1700000000,
    "exp": 1700086400
  },
  "signature": "xxx"
}
```

**Các field:**
- `sub` (subject): User ID
- `email`: Email của user
- `role`: Role (ADMIN, TUTOR, MENTEE)
- `iat` (issued at): Thời gian tạo token
- `exp` (expiration): Thời gian hết hạn

---

## 🛡️ Security Features

1. **Password Encoding**: BCrypt (10 rounds)
2. **JWT Signing**: HS512 algorithm
3. **HttpOnly Cookie**: Token không thể được truy cập từ JavaScript (chống XSS)
4. **CORS**: Cho phép localhost:3000 và localhost:5173
5. **Role-Based Access Control**: 3 roles - ADMIN, TUTOR, MENTEE

---

## 📱 Frontend Integration (JavaScript/React)

### Axios Interceptor
```javascript
import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true  // Tự động gửi cookies
});

// Interceptor cho error handling
instance.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // Redirect to login
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default instance;
```

### Login Example
```javascript
const login = async (email, password) => {
  try {
    const response = await instance.post(
      '/auth/login',
      null,
      { params: { email, password } }
    );
    console.log('Login successful:', response.data);
    // Cookie sẽ tự động được set bởi browser
  } catch (error) {
    console.error('Login failed:', error);
  }
};
```

### Access Protected Resource
```javascript
const getAdminData = async () => {
  try {
    const response = await instance.get('/admin/manage-user');
    console.log('Admin data:', response.data);
  } catch (error) {
    if (error.response?.status === 403) {
      console.error('Access denied - need ADMIN role');
    }
  }
};
```

---

## ⚠️ Production Checklist

- [ ] Thay đổi `app.jwt.secret` thành một key dài, ngẫu nhiên
- [ ] Set `cookie.setSecure(true)` khi dùng HTTPS
- [ ] Update CORS origins để chỉ cho phép domain production
- [ ] Thay đổi `spring.jpa.hibernate.ddl-auto` từ `update` sang `validate`
- [ ] Disable Swagger/OpenAPI tại production: thêm `springdoc.api-docs.enabled=false`

---

## 🐛 Troubleshooting

### Cookie không được set
- Kiểm tra `withCredentials: true` trong frontend
- Kiểm tra CORS setting

### Token hết hạn
- Default là 24 giờ, có thể thay đổi `app.jwt.expiration`
- Frontend nên có logic refresh token

### 403 Access Denied
- Kiểm tra role của user
- Đảm bảo token vẫn hiệu lực

---

## 📚 References

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JJWT Library](https://github.com/jwtk/jjwt)
- [JWT.io - Debugger](https://jwt.io/)
