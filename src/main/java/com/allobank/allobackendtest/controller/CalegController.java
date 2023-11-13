package com.allobank.allobackendtest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepo;
import com.allobank.allobackendtest.service.CalegService;

@RestController
@RequestMapping("/api/calegs")
public class CalegController {

	private CalegService calegService;

	@Autowired
	public CalegController(CalegService calegService) {
		this.calegService = calegService;
	}

	@GetMapping
	public List<Caleg> getAllCaleg() {
		return calegService.getAllCaleg();
	}

	@GetMapping("/orderByNomorUrut")
	public List<Caleg> getAllCalegOrderByNomorUrut() {
		return calegService.getAllCalegOrderByNoUrut();
	}

	@GetMapping("/{id}")
	public Caleg getCalegById(@PathVariable UUID id) {
		return calegService.getCalegById(id);
	}
}
