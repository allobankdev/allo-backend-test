package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CalegService {

    @Autowired
    private CalegRepository calegRepository;

    @Autowired
    private DapilRepository dapilRepository;

    @Autowired
    private PartaiRepository partaiRepository;

    public Map getCaleg(String partaiName, String dapilName, Boolean sortByNomorUrut) {
        Map result = new HashMap();
        List<Caleg> calegList = calegRepository.findAllFiltered(partaiName, dapilName, sortByNomorUrut);
        if (!calegList.isEmpty()){
            result.put("success",true);
            result.put("message","Success Get Caleg");
            result.put("data",calegList);
        }else {
            result.put("success",false);
            result.put("message","Data Not Found");
            result.put("data",calegList);
        }
        return result;
    }


    public Map createCaleg(Caleg caleg) {
        Map result = new HashMap<>();
        UUID dapilId = caleg.getDapil().getId();
        UUID partaiId = caleg.getPartai().getId();
        Dapil dapil = dapilRepository.findById(dapilId).orElse(null);
        Partai partai = partaiRepository.findById(partaiId).orElse(null);
        if (dapil!=null && partai!=null){
            caleg.setDapil(dapil);
            caleg.setPartai(partai);
            calegRepository.save(caleg);
            result.put("success",true);
            result.put("message","Success create Caleg");
            result.put("data",caleg);
        }
        else {
            result.put("success",false);
            result.put("message","Dapil ID or Partai ID not Exist");
            result.put("data",null);
        }
        return result;
    }

    public Map updateCaleg(UUID id, Caleg caleg) {
        caleg.setId(id);
        Map result = new HashMap<>();
        UUID dapilId = caleg.getDapil().getId();
        UUID partaiId = caleg.getPartai().getId();
        Dapil dapil = dapilRepository.findById(dapilId).orElse(null);
        Partai partai = partaiRepository.findById(partaiId).orElse(null);
        if (dapil!=null && partai!=null){
            caleg.setDapil(dapil);
            caleg.setPartai(partai);
            calegRepository.save(caleg);
            result.put("success",true);
            result.put("message","Success update Caleg : "+id);
            result.put("data",caleg);
        }
        else {
            result.put("success",false);
            result.put("message","Dapil ID or Partai ID not Exist");
            result.put("data",null);
        }
        return result;
    }
}

