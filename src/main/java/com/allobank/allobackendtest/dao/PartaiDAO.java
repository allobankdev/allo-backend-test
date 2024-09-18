package com.allobank.allobackendtest.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Partai;

@Repository
public interface PartaiDAO extends JpaRepository<Partai, UUID>{
	public Partai findByNamaPartai(String namaPartai);
}
