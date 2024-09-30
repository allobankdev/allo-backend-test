package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Dapil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DapilRepository extends JpaRepository<Dapil, Long> {

    Optional<Dapil> findById(Long id);

    @Query("SELECT d FROM Dapil d WHERE upper(d.namaDapil) = upper(:namaDapil)")
    List<Dapil> findByNamaDapilIgnoreCase(@Param("namaDapil") String namaDapil);
}
