package com.allobank.allobackendtest.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.entity.partaie;

@Repository
public interface partair extends JpaRepository<partaie,UUID>{
    Optional<partaie> findbyname(String partai);

}
