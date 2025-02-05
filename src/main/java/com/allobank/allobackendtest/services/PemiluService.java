package com.allobank.allobackendtest.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.entities.Caleg;
import com.allobank.allobackendtest.entities.Dapil;
import com.allobank.allobackendtest.entities.Partai;
import com.allobank.allobackendtest.models.CalegRequest;
import com.allobank.allobackendtest.models.DapilRequest;
import com.allobank.allobackendtest.models.PartaiRequest;
import com.allobank.allobackendtest.repositories.CalegRepository;
import com.allobank.allobackendtest.repositories.DapilRepository;
import com.allobank.allobackendtest.repositories.PartaiRepository;

@Service
public class PemiluService {
	@Autowired
	private CalegRepository calegRepository;
	@Autowired
	private DapilRepository dapilRepository;
	@Autowired
	private PartaiRepository partaiRepository;

	public List<Caleg> getCalegList(UUID dapilId, UUID partaiId) {
		return calegRepository.findByDapil_IdAndPartai_IdOrderByNomorUrut(dapilId, partaiId);
	}

	public Caleg createCaleg(CalegRequest request) {
		Dapil dapil = dapilRepository.findById(request.getDapilId())
				.orElseThrow(() -> new IllegalArgumentException("Dapil tidak ditemukan"));
		Partai partai = partaiRepository.findById(request.getPartaiId())
				.orElseThrow(() -> new IllegalArgumentException("Partai tidak ditemukan"));

		Caleg caleg = Caleg.builder().id(UUID.randomUUID()).dapil(dapil).partai(partai)
				.nomorUrut(request.getNomorUrut()).nama(request.getNama()).jenisKelamin(request.getJenisKelamin())
				.build();

		return calegRepository.save(caleg);
	}

	public Dapil createDapil(DapilRequest request) {
		Dapil dapil = Dapil.builder().id(UUID.randomUUID()).namaDapil(request.getNamaDapil())
				.provinsi(request.getProvinsi()).wilayahDapilList(request.getWilayahDapilList())
				.jumlahKursi(request.getJumlahKursi()).build();
		return dapilRepository.save(dapil);
	}

	public Partai createPartai(PartaiRequest request) {
		Partai partai = Partai.builder().id(UUID.randomUUID()).namaPartai(request.getNamaPartai())
				.nomorUrut(request.getNomorUrut()).build();
		return partaiRepository.save(partai);
	}
}