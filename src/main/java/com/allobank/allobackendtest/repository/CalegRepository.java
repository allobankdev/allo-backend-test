package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {

    List<Caleg> findByPartaiNamaPartaiAndDapilNamaDapilOrderByNomorUrut(String partaiName, String dapilName);

    List<Caleg> findByPartaiNamaPartaiAndDapilNamaDapil(String partaiName, String dapilName);

    List<Caleg> findByDapilNamaDapilOrderByNomorUrut(String dapilName);

    List<Caleg> findByDapilNamaDapil(String dapilName);

    List<Caleg> findByPartaiNamaPartaiOrderByNomorUrut(String partaiName);

    List<Caleg> findByPartaiNamaPartai(String partaiName);

    List<Caleg> findAllByOrderByNomorUrut();

    default List<Caleg> findAllFiltered(String partaiName, String dapilName, boolean sortByNomorUrut) {
        if (partaiName == null && dapilName == null) {
            return sortByNomorUrut ? findAllByOrderByNomorUrut() : findAll();
        } else if (partaiName == null) {
            return sortByNomorUrut ? findByDapilNamaDapilOrderByNomorUrut(dapilName) : findByDapilNamaDapil(dapilName);
        } else if (dapilName == null) {
            return sortByNomorUrut ? findByPartaiNamaPartaiOrderByNomorUrut(partaiName) : findByPartaiNamaPartai(partaiName);
        } else {
            return sortByNomorUrut ? findByPartaiNamaPartaiAndDapilNamaDapilOrderByNomorUrut(partaiName, dapilName) : findByPartaiNamaPartaiAndDapilNamaDapil(partaiName, dapilName);
        }
    }
}

