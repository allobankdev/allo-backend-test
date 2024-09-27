package com.allobank.allobackendtest.services;

import com.allobank.allobackendtest.dto.PartaiDto;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PartaiService {

    @Autowired
    PartaiRepository partaiRepository;

    public void addPartaiSvc(PartaiDto partaiDto){
        Partai partai = new Partai();

        try {
            partai.setNamaPartai(partaiDto.getNamaPartai());
            partai.setSingkatanPartai(partaiDto.getSingkatanPartai());
            partai.setCreatedAt(new Date());
            partaiRepository.save(partai);
        }catch (Exception e){
            System.out.print("addPartaiSvc: ");
            e.printStackTrace();
        }

    }
}
