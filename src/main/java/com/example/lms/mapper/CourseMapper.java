package com.example.lms.mapper;

import com.example.lms.dto.CourseDTO;
import com.example.lms.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }

        Long tutorId = null;
        String tutorName = null;
        Long subjectRegistrationId = null;

        // Extract tutor and subject registration info safely
        if (course.getSubjectRegistration() != null) {
            subjectRegistrationId = course.getSubjectRegistration().getId();
            if (course.getSubjectRegistration().getTutor() != null) {
                tutorId = course.getSubjectRegistration().getTutor().getId();
                tutorName = course.getSubjectRegistration().getTutor().getName();
            }
        }

        return CourseDTO.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .description(course.getDescription())
                .maxMentee(course.getMaxMentee())
                .courseStatus(course.getCourseStatus() != null ? course.getCourseStatus().name() : null)
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .createdDate(course.getCreatedDate())
                .subjectRegistrationId(subjectRegistrationId)
                .tutorId(tutorId)
                .tutorName(tutorName)
                .totalEnrollments(course.getEnrollments().size())
                .build();
    }

    public Course toEntity(CourseDTO dto) {
        if (dto == null) {
            return null;
        }

        return Course.builder()
                .courseId(dto.getCourseId())
                .courseName(dto.getCourseName())
                .description(dto.getDescription())
                .maxMentee(dto.getMaxMentee())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .createdDate(dto.getCreatedDate())
                .build();
    }
}
