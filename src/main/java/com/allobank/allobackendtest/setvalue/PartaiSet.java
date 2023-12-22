package com.allobank.allobackendtest.setvalue;

import java.util.stream.Collectors;

import java.util.List;

import com.allobank.allobackendtest.entity.partaie;
import com.allobank.allobackendtest.model.Partai;

public class PartaiSet {
    public static Partai partai(partaie partaiEntity){
        Partai partai = new Partai();
        partai.setId(partaiEntity.getId());
        partai.setNamaPartai(partaiEntity.getNamaPartai());
        partai.setNomorUrut(partaiEntity.getNomorUrut());
        return partai;
    }

    public static List<Partai> partaiList(List<partaie> enList) {
        return enList.stream().map(PartaiSet::partai).collect(Collectors.toList());
    }
}
