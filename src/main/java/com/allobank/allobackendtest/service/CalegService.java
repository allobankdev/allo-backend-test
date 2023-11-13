package com.allobank.allobackendtest.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepo;

@Service
public class CalegService {

	private CalegRepo calegRepo;

	@Autowired
	public CalegService(CalegRepo calegRepo) {
		this.calegRepo = calegRepo;
	}

	public List<Caleg> getAllCaleg() {
		return (List<Caleg>) calegRepo.findAll();
	}

	public List<Caleg> getAllCalegOrderByNoUrut() {
		return (List<Caleg>) calegRepo.findAllByOrderByNomorUrutAsc();
	}

	public Caleg getCalegById(UUID id) {
		return calegRepo.findById(id).orElse(null);
	}
}
