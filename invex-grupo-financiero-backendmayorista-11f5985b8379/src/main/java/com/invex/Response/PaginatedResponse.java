package com.invex.Response;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> items; // Cambiado a letra minúscula
    private long totalRecords;
    private int totalPages;

    // Getters y Setters

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public PaginatedResponse(List<T> items, long totalRecords, int totalPages) {
        this.items = items;
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
    }
}
