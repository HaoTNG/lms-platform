package com.example.lms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pagination {
    private Object content;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
