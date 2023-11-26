package com.allobank.allobackendtest.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DapilResponse {

    @JsonProperty("nama_dapil")
    private String namaDapil;

    @JsonProperty("provinsi")
    private String provinsi;

    @JsonProperty("wilayah_dapil")
    private List<String> wilayahDapilList;

    @JsonProperty("jumlah_kursi")
    private int jumlahKursi;
}
