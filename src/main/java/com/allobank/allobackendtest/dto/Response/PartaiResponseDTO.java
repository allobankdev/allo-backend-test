package com.allobank.allobackendtest.dto.Response;

import lombok.Data;

import java.util.UUID;

@Data
public class PartaiResponseDTO {
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
