package com.mindvault.mymemory.dto;

import com.mindvault.mymemory.entity.Memory;
import java.util.List;

public class PaginatedMemoryResponse {
    private List<Memory> data;
    private Meta meta;

    public PaginatedMemoryResponse(List<Memory> data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    // getters & setters
    public List<Memory> getData() { return data; }
    public void setData(List<Memory> data) { this.data = data; }
    public Meta getMeta() { return meta; }
    public void setMeta(Meta meta) { this.meta = meta; }
}