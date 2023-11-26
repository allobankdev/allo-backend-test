package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Caleg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, UUID> {

    @Query("SELECT c FROM Caleg c WHERE c.partai.namaPartai LIKE %?1% AND c.dapil.namaDapil LIKE %?2% ORDER BY c.nomorUrut")
    List<Caleg> getCalegByPartaiIdAndDApilId(String namePartai, String namaDapil);
}
