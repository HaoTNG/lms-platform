package com.example.lms.dto;

import lombok.Data;

@Data
public class Pagination {
    private int pageNumber;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
