package com.allobank.allobackendtest.dto.Request;

import com.allobank.allobackendtest.entity.JenisKelaminEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class CalegRequestDTO {
    private UUID dapilId;
    private UUID partaiId;
    private Integer nomorUrut;
    private String nama;
    private JenisKelaminEnum jenisKelamin;
}
