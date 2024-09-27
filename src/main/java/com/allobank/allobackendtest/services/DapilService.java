package com.allobank.allobackendtest.services;

import com.allobank.allobackendtest.dto.DapilDto;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DapilService {
    @Autowired
    DapilRepository dapilRepository;

    public void addDapilSvc(DapilDto dapilDto){
        Dapil dapil = new Dapil();

        try{
            dapil.setNamaDapil(dapilDto.getNamaDapil());
            dapil.setWilayah(dapilDto.getWilayah());
            dapil.setAlokasiKursi(dapilDto.getAlokasiKursi());
            dapil.setCreatedAt(new Date());
            dapilRepository.save(dapil);
        }catch (Exception e){
            System.out.print("addDapilSvc: ");
            e.printStackTrace();
        }
    }
}
