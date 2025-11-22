package com.example.lms.enums;

import lombok.Getter;

@Getter
public enum ReportTicketStatus {
    PENDING("Pending"),
    RESOLVED("Resolved"),
    REJECTED("Rejected");
    
    private final String displayName;
    ReportTicketStatus(String displayName) {
        this.displayName = displayName;
    }

    public static ReportTicketStatus fromValue(String value) {
        for (ReportTicketStatus cs : values()) {
            if (cs.name().equalsIgnoreCase(value)) {
                return cs;
            }
        }
        throw new IllegalArgumentException("Invalid Status: " + value);
    }
}
