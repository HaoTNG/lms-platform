package com.example.lms.enums;

public enum EnrollmentStatus {
    PENDING,    // Chờ thanh toán/duyệt (nếu khóa học có phí)
    ACTIVE,     // Đang học chính thức
    COMPLETED,  // Đã học xong
    DROPPED,    // Đã bỏ ngang
    FAILED      // Trượt
}