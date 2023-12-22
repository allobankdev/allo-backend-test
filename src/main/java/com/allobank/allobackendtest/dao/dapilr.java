package com.allobank.allobackendtest.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.entity.dapile;

@Repository
public interface dapilr extends JpaRepository<dapile,UUID>{
Optional<dapile> findByName(String dapil);
    
}