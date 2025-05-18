package com.allobank.allobackendtest.util.dto;

import com.allobank.allobackendtest.model.Partai;
import lombok.Data;

import java.util.UUID;

@Data
public class PartaiResponseDTO {
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;

    public PartaiResponseDTO(Partai partai) {
        this.id = partai.getId();
        this.namaPartai = partai.getNamaPartai();
        this.nomorUrut = partai.getNomorUrut();
    }
}
