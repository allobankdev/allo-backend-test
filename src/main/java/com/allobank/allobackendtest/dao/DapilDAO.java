package com.allobank.allobackendtest.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Dapil;

@Repository
public interface DapilDAO extends JpaRepository<Dapil, UUID>{
	public Dapil findByNamaDapil(String namaDapil);
}
