package com.frankfurter.aggregator.dto.internal;
import java.time.LocalDateTime;
import java.util.Map;


public class FinanceDataResponse {
    private String resourceType;
    private LocalDateTime fetchedAt;
    private Map<String, Object> data;
    
    public FinanceDataResponse() {}
    public FinanceDataResponse(String resourceType, LocalDateTime fetchedAt, Map<String, Object> data) {
        this.resourceType = resourceType;
        this.fetchedAt = fetchedAt;
        this.data = data;
    }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public LocalDateTime getFetchedAt() { return fetchedAt; }
    public void setFetchedAt(LocalDateTime fetchedAt) { this.fetchedAt = fetchedAt; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}
