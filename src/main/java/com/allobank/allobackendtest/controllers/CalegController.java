package com.allobank.allobackendtest.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.entities.Caleg;
import com.allobank.allobackendtest.entities.Dapil;
import com.allobank.allobackendtest.entities.Partai;
import com.allobank.allobackendtest.models.CalegRequest;
import com.allobank.allobackendtest.models.DapilRequest;
import com.allobank.allobackendtest.models.PartaiRequest;
import com.allobank.allobackendtest.services.PemiluService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pemilu")
@Tag(name = "Pemilu API", description = "API untuk mengelola data pemilu")
public class CalegController {
	@Autowired
	private PemiluService pemiluService;

	@Operation(summary = "Lihat Caleg", description = "Endpoint untuk melihat daftar Caleg di sistem.")
	@GetMapping("/caleg")
	public ResponseEntity<List<Caleg>> getCalegList(@RequestParam UUID dapilId, @RequestParam UUID partaiId) {
		List<Caleg> calegList = pemiluService.getCalegList(dapilId, partaiId);
		return ResponseEntity.ok(calegList);
	}

	@Operation(summary = "Tambah Caleg", description = "Endpoint untuk menambahkan data Caleg baru ke dalam sistem.")
	@PostMapping("/caleg")
	public ResponseEntity<Caleg> createCaleg(@RequestBody CalegRequest request) {
		Caleg caleg = pemiluService.createCaleg(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(caleg);
	}

	@Operation(summary = "Tambah Dapil", description = "Endpoint untuk menambahkan data Dapil baru ke dalam sistem.")
	@PostMapping("/dapil")
	public ResponseEntity<Dapil> createDapil(@RequestBody DapilRequest request) {
		Dapil dapil = pemiluService.createDapil(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(dapil);
	}

	@Operation(summary = "Tambah Partai", description = "Endpoint untuk menambahkan data Partai baru ke dalam sistem.")
	@PostMapping("/partai")
	public ResponseEntity<Partai> createPartai(@RequestBody PartaiRequest request) {
		Partai partai = pemiluService.createPartai(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(partai);
	}
}