package com.allobank.allobackendtest.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartaiResponse {

    @JsonProperty("name_partai")
    private String namaPartai;

    @JsonProperty("nomor_urut")
    private Integer nomorUrut;
}
