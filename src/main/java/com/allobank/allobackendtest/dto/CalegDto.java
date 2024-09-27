package com.allobank.allobackendtest.dto;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class CalegDto {
    private int id;
    private String namaCaleg;
    private int nomorUrut;
    private Partai partai;
    private Dapil dapil;
    private Date createdAt;
}
