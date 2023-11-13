package com.allobank.allobackendtest.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Caleg;

@Repository
public interface CalegRepo extends CrudRepository<Caleg, UUID> {
	public List<Caleg> findAllByOrderByNomorUrutAsc();
}
