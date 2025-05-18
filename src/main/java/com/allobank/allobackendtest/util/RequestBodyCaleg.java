package com.allobank.allobackendtest.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBodyCaleg {
    private String dapil;
    private String partai;
    private Integer nomorUrut;
    private String nama;
    private String jenisKelamin;
}
