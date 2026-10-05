package com.finance.aggregator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedResponseDTO {
    private String resourceType;
    private Object data;
    private Long timestamp;
    private String status;
}