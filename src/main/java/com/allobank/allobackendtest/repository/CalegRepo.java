package com.allobank.allobackendtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Caleg;

@Repository
public interface CalegRepo extends JpaRepository<Caleg, String>{

    @Query(value = "select * from test.caleg a, test.dapil b, test.partai c where a.Partai = c.NamaPartai and a.Dapil = b.NamaDapil and a.Dapil = :nama_dapil and b.partai = :nama_partai", nativeQuery =  true)
    public String getListCaleg(@Param("nama_dapil") String dapil, @Param("nama_partai") String partai);

} 
