package com.mindvault.mymemory.dto;

public class MemoryResponse {
    private Long id;
    private String desc;

    public MemoryResponse(Long id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
}
