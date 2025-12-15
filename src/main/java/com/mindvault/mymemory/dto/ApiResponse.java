package com.mindvault.mymemory.dto;

import java.util.List;

public class ApiResponse<T> {
    private String message;
    private boolean status;
    private List<T> data;
    private Meta meta;

    public ApiResponse(String message, boolean status, List<T> data, Meta meta) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.meta = meta;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public List<T> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public static class Meta {
        private int currentPage;
        private int pageSize;
        private int totalPages;
        private long totalItems;
        private long total;
        private int limit;

        public Meta(int currentPage, int pageSize, int totalPages, long totalItems, long total, int limit) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
            this.totalItems = totalItems;
            this.total = total;
            this.limit = limit;
        }

        public int getCurrentPage() { return currentPage; }
        public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        public long getTotalItems() { return totalItems; }
        public void setTotalItems(long totalItems) { this.totalItems = totalItems; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
    }
}
