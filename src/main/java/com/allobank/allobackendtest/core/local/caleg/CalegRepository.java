package com.allobank.allobackendtest.core.local.caleg;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.core.local.caleg.model.Caleg;

@Repository
public interface CalegRepository extends JpaRepository<Caleg, Long> {

    Optional<Caleg> findOneByNama(String nama);

    @Query("SELECT c FROM Caleg c WHERE c.dapil.namaDapil LIKE %:dapilNama% AND c.partai.namaPartai LIKE %:partaiNama%")
    List<Caleg> findByDapilAndPartai(@Param("dapilNama") String dapilNama, @Param("partaiNama") String partaiNama);
    
}
