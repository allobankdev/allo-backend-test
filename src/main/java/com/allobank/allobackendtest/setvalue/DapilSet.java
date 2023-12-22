package com.allobank.allobackendtest.setvalue;


import java.util.stream.Collectors;

import java.util.List;

import com.allobank.allobackendtest.entity.dapile;
import com.allobank.allobackendtest.model.Dapil;

public class DapilSet {
    public static Dapil dapil (dapile dapilEntity){
        Dapil dapil = new Dapil();
        dapil.setId(dapilEntity.getId());
        dapil.setJumlahKursi(dapilEntity.getJumlahKursi());
        dapil.setNamaDapil(dapilEntity.getNamaDapil());
        dapil.setProvinsi(dapilEntity.getProvinsi());
        dapil.setWilayahDapilList(dapilEntity.getWilayahDapilList());
        return dapil;
    }

    public static List<Dapil> dapiList(List<dapile> enList){
        return enList.stream().map(DapilSet::dapil).collect(Collectors.toList());
    }
}

