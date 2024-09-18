package com.allobank.allobackendtest.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;

@Repository
public interface CalegDAO extends JpaRepository<Caleg, UUID>{
	public List<Caleg> findByPartai(Partai partai, Sort sort);
	
	public List<Caleg> findByDapil(Dapil dapil,Sort sort); 
	
}
