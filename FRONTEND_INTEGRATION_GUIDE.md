# Frontend Integration Guide - LMS Platform

## 🌐 Backend Configuration

**Backend URL:** `http://localhost:8080`
**Frontend URL:** `http://localhost:5174`

✅ **CORS đã được cấu hình để allow:**
- ✅ `http://localhost:5174`
- ✅ `http://127.0.0.1:5174`
- ✅ Credentials (cookies)
- ✅ Tất cả HTTP methods (GET, POST, PUT, DELETE, OPTIONS, PATCH)

---

## 📝 JavaScript/Frontend Code Examples

### 1️⃣ **Register (Đăng ký)**

```javascript
async function register(name, email, password, role = 'MENTEE') {
  try {
    const response = await fetch('http://localhost:8080/api/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',  // ⭐ Quan trọng! Gửi/nhận cookies
      body: JSON.stringify({
        name,
        email,
        password,
        role
      })
    });

    const data = await response.json();
    
    if (response.ok) {
      console.log('✅ Đăng ký thành công:', data.user);
      // Cookie tự động được set, không cần xử lý
      return data;
    } else {
      console.error('❌ Đăng ký thất bại:', data.message);
      return null;
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Usage
await register('John Doe', 'john@example.com', 'password123', 'MENTEE');
```

---

### 2️⃣ **Login (Đăng nhập)**

```javascript
async function login(email, password) {
  try {
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',  // ⭐ Gửi/nhận cookies
      body: JSON.stringify({
        email,
        password
      })
    });

    const data = await response.json();
    
    if (response.ok) {
      console.log('✅ Đăng nhập thành công:', data.user);
      // Cookie tự động được set và lưu trữ
      localStorage.setItem('user', JSON.stringify(data.user));
      return data.user;
    } else {
      console.error('❌ Đăng nhập thất bại:', data.message);
      return null;
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Usage
const user = await login('john@example.com', 'password123');
if (user) {
  console.log('Logged in as:', user.name);
}
```

---

### 3️⃣ **Check Authentication (Kiểm tra đăng nhập)**

```javascript
async function getCurrentUser() {
  try {
    const response = await fetch('http://localhost:8080/api/auth/me', {
      method: 'GET',
      credentials: 'include',  // ⭐ Gửi cookie
    });

    const data = await response.json();
    
    if (response.ok) {
      console.log('✅ Đã xác thực:', data.user);
      return data.user;
    } else {
      console.log('⚠️ Chưa đăng nhập:', data.message);
      return null;
    }
  } catch (error) {
    console.error('Error:', error);
    return null;
  }
}

// Usage
const user = await getCurrentUser();
if (user) {
  console.log('User:', user.name, '- Role:', user.role);
}
```

---

### 4️⃣ **Logout (Đăng xuất)**

```javascript
async function logout() {
  try {
    const response = await fetch('http://localhost:8080/api/auth/logout', {
      method: 'POST',
      credentials: 'include',  // ⭐ Gửi cookie để xóa
    });

    const data = await response.json();
    
    if (response.ok) {
      console.log('✅ Đăng xuất thành công');
      localStorage.removeItem('user');
      // Cookie tự động expired
      return true;
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Usage
await logout();
```

---

### 5️⃣ **Get Users (Admin) - Lấy danh sách user**

```javascript
async function getUsers(page = 0, size = 10, role = null, search = null) {
  try {
    let url = `http://localhost:8080/api/admin/manage-user?page=${page}&size=${size}`;
    
    if (role) url += `&role=${role}`;        // Filter: MENTEE, TUTOR, ADMIN
    if (search) url += `&search=${search}`;  // Search term

    const response = await fetch(url, {
      method: 'GET',
      credentials: 'include',  // ⭐ Cookie phải có để xác thực ADMIN
    });

    const data = await response.json();
    
    if (response.ok) {
      console.log('✅ Danh sách user:', data.data);
      return data.data;
    } else {
      console.error('❌ Lỗi:', data.message);
      return [];
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Usage - Lấy 20 mentees trang thứ 0
const users = await getUsers(0, 20, 'MENTEE', null);
```

---

### 6️⃣ **Get System Analytics (Admin)**

```javascript
async function getSystemStats() {
  try {
    const response = await fetch('http://localhost:8080/api/admin/analytics/system', {
      method: 'GET',
      credentials: 'include',  // ⭐ Cookie
    });

    const data = await response.json();
    
    if (response.ok) {
      const stats = data.data;
      console.log('✅ Thống kê hệ thống:');
      console.log(`   Total Users: ${stats.totalUsers}`);
      console.log(`   Total Admins: ${stats.totalAdmins}`);
      console.log(`   Total Tutors: ${stats.totalTutors}`);
      console.log(`   Total Mentees: ${stats.totalMentees}`);
      console.log(`   Total Courses: ${stats.totalCourses}`);
      console.log(`   Active Courses: ${stats.activeCourses}`);
      return stats;
    } else {
      console.error('❌ Lỗi:', data.message);
      return null;
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Usage
await getSystemStats();
```

---

### 7️⃣ **Send Announcement (Admin)**

```javascript
async function sendAnnouncement(title, content, recipientType = 'ALL') {
  try {
    let endpoint = '/api/admin/announcements/send-all';
    
    if (recipientType === 'MENTEE') {
      endpoint = '/api/admin/announcements/send-mentee';
    } else if (recipientType === 'TUTOR') {
      endpoint = '/api/admin/announcements/send-tutor';
    }

    const response = await fetch(`http://localhost:8080${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',  // ⭐ Cookie
      body: JSON.stringify({
        title,
        content,
        recipientType
      })
    });

    const data = await response.json();
    
    if (response.ok) {
      console.log('✅ Gửi thông báo thành công:', data.data);
      return data.data;
    } else {
      console.error('❌ Lỗi:', data.message);
      return null;
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Usage
await sendAnnouncement(
  'System Maintenance',
  'Server will be down for maintenance tonight',
  'ALL'
);
```

---

### 8️⃣ **Create Course (Admin)**

```javascript
async function createCourse(courseName, description, maxMentee, subjectRegistrationId) {
  try {
    const response = await fetch('http://localhost:8080/api/admin/courses', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',  // ⭐ Cookie
      body: JSON.stringify({
        courseName,
        description,
        maxMentee,
        subjectRegistration: {
          id: subjectRegistrationId
        }
      })
    });

    const data = await response.json();
    
    if (response.ok) {
      console.log('✅ Tạo khóa học thành công:', data.data);
      return data.data;
    } else {
      console.error('❌ Lỗi:', data.message);
      return null;
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Usage
await createCourse('Java 101', 'Intro to Java', 50, 1);
```

---

## 🔧 Utility Class (TypeScript/JavaScript)

```typescript
class LMSApi {
  private baseUrl = 'http://localhost:8080';
  
  private async request(endpoint: string, options: RequestInit = {}) {
    const url = `${this.baseUrl}${endpoint}`;
    
    // Merge options với defaults
    const mergedOptions: RequestInit = {
      credentials: 'include', // ⭐ Always include cookies
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      }
    };

    const response = await fetch(url, mergedOptions);
    const data = await response.json();
    
    if (!response.ok) {
      console.error(`❌ Error: ${data.message}`);
      throw new Error(data.message);
    }
    
    return data;
  }

  // Auth
  async register(name: string, email: string, password: string, role: string = 'MENTEE') {
    return this.request('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify({ name, email, password, role })
    });
  }

  async login(email: string, password: string) {
    return this.request('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });
  }

  async logout() {
    return this.request('/api/auth/logout', { method: 'POST' });
  }

  async getCurrentUser() {
    return this.request('/api/auth/me');
  }

  // Admin - Users
  async getUsers(page: number = 0, size: number = 10, role?: string, search?: string) {
    let url = `/api/admin/manage-user?page=${page}&size=${size}`;
    if (role) url += `&role=${role}`;
    if (search) url += `&search=${search}`;
    return this.request(url);
  }

  // Admin - Analytics
  async getSystemStats() {
    return this.request('/api/admin/analytics/system');
  }

  async getStudentAnalytics() {
    return this.request('/api/admin/analytics/students');
  }

  async getTutorAnalytics() {
    return this.request('/api/admin/analytics/tutors');
  }

  async getAllAnalytics() {
    return this.request('/api/admin/analytics');
  }

  // Admin - Announcements
  async sendAnnouncement(title: string, content: string, recipientType: string = 'ALL') {
    const endpoints: { [key: string]: string } = {
      'ALL': '/api/admin/announcements/send-all',
      'MENTEE': '/api/admin/announcements/send-mentee',
      'TUTOR': '/api/admin/announcements/send-tutor'
    };
    
    return this.request(endpoints[recipientType], {
      method: 'POST',
      body: JSON.stringify({ title, content, recipientType })
    });
  }

  async getCourses(page: number = 0, size: number = 10, filters?: { tutor?: string; status?: string; course_name?: string }) {
    let url = `/api/admin/courses?page=${page}&size=${size}`;
    if (filters?.tutor) url += `&tutor=${filters.tutor}`;
    if (filters?.status) url += `&status=${filters.status}`;
    if (filters?.course_name) url += `&course_name=${filters.course_name}`;
    return this.request(url);
  }

  async createCourse(courseName: string, description: string, maxMentee: number, subjectRegistrationId: number) {
    return this.request('/api/admin/courses', {
      method: 'POST',
      body: JSON.stringify({
        courseName,
        description,
        maxMentee,
        subjectRegistration: { id: subjectRegistrationId }
      })
    });
  }
}

// Usage
const api = new LMSApi();

// Login
const user = await api.login('admin@example.com', 'password123');
console.log('✅ Logged in as:', user.user.name);

// Get stats
const stats = await api.getSystemStats();
console.log('Total users:', stats.data.totalUsers);

// Send announcement
await api.sendAnnouncement('Welcome', 'Welcome to LMS', 'ALL');
```

---

## 🚨 Common Issues & Solutions

### ❌ Issue: CORS Error

```
Access to XMLHttpRequest at 'http://localhost:8080/api/auth/login' from origin 
'http://localhost:5174' has been blocked by CORS policy
```

**Solution:**
- ✅ Kiểm tra `credentials: 'include'` trong fetch
- ✅ Backend CORS đã allow `http://localhost:5174`
- ✅ Server đang chạy ở port 8080

### ❌ Issue: Cookie không được gửi

```javascript
// ❌ WRONG
fetch('http://localhost:8080/api/admin/manage-user')

// ✅ CORRECT
fetch('http://localhost:8080/api/admin/manage-user', {
  credentials: 'include'  // ⭐ REQUIRED!
})
```

### ❌ Issue: 401 Unauthorized trên Admin endpoints

```
{
  "statusCode": 401,
  "message": "Unauthorized"
}
```

**Solutions:**
1. Kiểm tra user có role ADMIN không: `user.role === 'ADMIN'`
2. Kiểm tra cookie có được set không: Check DevTools → Application → Cookies
3. Kiểm tra token chưa hết hạn: Token có hiệu lực 24h

---

## 🔍 Testing với Postman/Thunder Client

### 1. Register
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "name": "Admin User",
  "email": "admin@example.com",
  "password": "admin123",
  "role": "ADMIN"
}
```

### 2. Login
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "Login successful",
  "user": {
    "id": 1,
    "name": "Admin User",
    "email": "admin@example.com",
    "role": "ADMIN"
  }
}
```

**Cookie được set:** Copy từ `Set-Cookie` header hoặc DevTools

### 3. Get Users (copy cookie từ login)
```
GET http://localhost:8080/api/admin/manage-user?page=0&size=10
Cookie: Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 📋 Checklist trước khi deploy

- ✅ Backend server chạy ở port 8080
- ✅ Frontend chạy ở port 5174
- ✅ CORS configuration include cả 2 URLs
- ✅ Fetch requests có `credentials: 'include'`
- ✅ Test login/logout flow
- ✅ Test admin endpoints với ADMIN role
- ✅ Kiểm tra cookies được set/clear
- ✅ Test role-based access control

---

## 🎯 Ports & URLs

| Service | URL | Port |
|---------|-----|------|
| Backend API | http://localhost:8080 | 8080 |
| Frontend App | http://localhost:5174 | 5174 |
| Database | (internal) | 3306 |

---

Bây giờ bạn có thể fetch data từ backend bình thường! 🚀
