package com.allobank.allobackendtest.payloads.response;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.Partai;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CalegResponse {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("nomor_urut")
    private Integer nomorUrut;

    @JsonProperty("nama")
    private String nama;

    @JsonProperty("jenis_kelamin")
    private JenisKelamin jenisKelamin;

    @JsonProperty("dapil")
    private DapilResponse dapil;

    @JsonProperty("partai")
    private PartaiResponse partai;

}
