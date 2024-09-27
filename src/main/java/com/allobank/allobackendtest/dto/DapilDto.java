package com.allobank.allobackendtest.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class DapilDto {
    private int id;
    private String namaDapil;
    private String wilayah;
    private int alokasiKursi;
    private Date createdAt;
}
