package com.allobank.allobackendtest.dto;

import com.allobank.allobackendtest.model.JenisKelamin;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CalegResponse(
        UUID id,
        Integer nomorUrut,
        String nama,
        JenisKelamin jenisKelamin
) {
}

