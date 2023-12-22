package com.allobank.allobackendtest.setvalue;

import java.util.stream.Collectors;

import java.util.List;

import com.allobank.allobackendtest.entity.calege;
import com.allobank.allobackendtest.model.Caleg;



public class CalegSet{
    public static Caleg caleg(calege entityCalege){
        Caleg caleg = new Caleg();
        caleg.setId(entityCalege.getId());
        caleg.setNama(entityCalege.getNama());
        caleg.setNomorUrut(entityCalege.getNomorUrut());
        caleg.setDapil(DapilSet.dapil(entityCalege.getDapil()));
        caleg.setPartai(PartaiSet.partai(entityCalege.getPartai()));
        caleg.setJenisKelamin(entityCalege.getJenisKelamin());
        return caleg;
    }

    public static List<Caleg> calegList(List<calege> enList){
        return enList.stream().map(CalegSet::caleg).collect(Collectors.toList());
    }
        
}