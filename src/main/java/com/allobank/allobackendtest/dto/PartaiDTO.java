package com.allobank.allobackendtest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartaiDTO {
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
