package com.allobank.allobackendtest.dao;

import com.allobank.allobackendtest.dto.CalegResponseDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CalegDao {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<CalegResponseDto> getListCaleg(String dapil, String partai) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT Caleg.id, Dapil.nama_dapil, Partai.nama_partai, ")
                .append("Caleg.nomor_urut, Caleg.nama, Caleg.jenis_kelamin ")
                .append("FROM Caleg ")
                .append("JOIN Dapil ON Dapil.id = Caleg.dapil ")
                .append("JOIN Partai ON Partai.id = Caleg.partai ")
                .append("WHERE 1 = 1" );

        if (dapil != null) {
            sb.append("AND Dapil.nama_dapil ILIKE :dapil ");
        }

        if (partai != null) {
            sb.append("AND Partai.nama_partai ILIKE :partai ");
        }
        sb.append("ORDER BY Caleg.nomor_urut ");

        Query query = em.createNativeQuery(sb.toString());

        if (dapil != null) {
            query.setParameter("dapil","%"+dapil+"%");
        }

        if (partai != null) {
            query.setParameter("partai","%"+partai+"%");
        }

        List<Object[]> list = query.getResultList();
        List<CalegResponseDto> listResult = new ArrayList<>();
        for (Object[] o : list) {
            CalegResponseDto response = new CalegResponseDto();
            response.setId((String) o[0]);
            response.setDapil((String) o[1]);
            response.setPartai((String) o[2]);
            response.setNomorUrut((Integer) o[3]);
            response.setNama((String) o[4]);
            response.setJenisKelamin((String) o[5]);
            listResult.add(response);
        }

        return listResult;
    }
}