package com.example.lms.enums;

public enum ResourceType {
    VIDEO,
    PDF,
    DOC,
    LINK,
    SOURCE_CODE;

    public static ResourceType fromValue(String value) {
        for (ResourceType cs : values()) {
            if (cs.name().equalsIgnoreCase(value)) {
                return cs;
            }
        }
        throw new IllegalArgumentException("Invalid ResourceType: " + value);
    }
}
