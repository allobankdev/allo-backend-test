package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.util.BasicUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PemiluService {
    Logger log = LoggerFactory.getLogger(PemiluService.class);

    @PersistenceContext
    EntityManager entityManager;

    public ResponseEntity<List<Map<String, Object>>> getListCaleg(Map<String,Object> param) throws Exception {
        log.info("Request getListCaleg {}", param);

        String namaPartai = (String) param.get("namaPartai");
        String namaDapil = (String) param.get("namaDapil");
        String provinsi = (String) param.get("provinsi");

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.nama, case when c.jenis_kelamin=0 then 'LAKILAKI' else 'PEREMPUAN' end, " +
                "c.nomor_urut, d.nama_dapil, d.provinsi, p.nama_partai " +
                "FROM caleg c inner join dapil d on c.dapil_id=d.id " +
                "inner join partai p on c.partai_id = p.id WHERE 1=1 ");

        if(namaPartai != null){
            sb.append("AND p.nama_partai = '").append(namaPartai).append("' ");
        }

        if(namaDapil != null){
            sb.append("AND d.nama_dapil = '").append(namaDapil).append("' ");
        }

        if(provinsi != null){
            sb.append("AND d.provinsi = '").append(provinsi).append("' ");
        }

        sb.append("ORDER BY c.nomor_urut");

        Query query = entityManager.createNativeQuery(sb.toString());

        String[] columnNameSeq = new String[] { "namaCaleg", "jenisKelamin", "nomorUrut", "namaDapil", "provinsi","namaPartai" };

        return ResponseEntity.ok(BasicUtils.createListOfMapFromArray(query.getResultList(), columnNameSeq));
    }
}