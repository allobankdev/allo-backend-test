package com.allobank.allobackendtest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CalegFilterDTO {
    private UUID dapilId;
    private UUID partaiId;
}