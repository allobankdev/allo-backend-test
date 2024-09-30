package com.allobank.allobackendtest.dto;


import com.allobank.allobackendtest.model.JenisKelamin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalegRequest {

    private Long id;
    private Long dapilId;
    private Long partaiId;
    private Integer nomorUrut;
    private String nama;
    private JenisKelamin jenisKelamin;
}
