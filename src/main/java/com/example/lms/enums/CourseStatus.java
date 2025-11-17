package com.example.lms.enums;

import lombok.Getter;

@Getter
public enum CourseStatus {
    OPEN,
    END;

    public static CourseStatus fromValue(String value) {
        for (CourseStatus cs : values()) {
            if (cs.name().equalsIgnoreCase(value)) {
                return cs;
            }
        }
        throw new IllegalArgumentException("Invalid CourseStatus: " + value);
    }
}


