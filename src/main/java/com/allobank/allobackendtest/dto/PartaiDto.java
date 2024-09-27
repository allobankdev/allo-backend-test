package com.allobank.allobackendtest.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class PartaiDto {
    private int id;
    private String namaPartai;
    private String singkatanPartai;
    private Date createdAt;

}
