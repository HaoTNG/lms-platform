package com.example.lms.dto;

import lombok.Data;

@Data
public class EnrollCourseRequest {
    private Long courseId;
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
