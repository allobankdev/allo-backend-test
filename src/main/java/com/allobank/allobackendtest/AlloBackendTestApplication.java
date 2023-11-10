package com.allobank.allobackendtest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@SpringBootApplication
@Log4j2
public class AlloBackendTestApplication implements CommandLineRunner {

	private final DapilRepository dapilRepository;
	private final PartaiRepository partaiRepository;
	private final CalegRepository calegRepository;

	public static void main(String[] args) {
		SpringApplication.run(AlloBackendTestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Initializing database....");
		int i = 0;
		int j = 3;
		while (i ++< 3) {
			Dapil dapil1 = dapilRepository.saveAndFlush(new Dapil("Nama_Dapil_"+i));
			Partai partai1 = partaiRepository.saveAndFlush(new Partai("Nama_Partai_"+i));
			calegRepository.save(new Caleg(dapil1, partai1, j, "Nama_Caleg_"+i));
			j--;
		}
		log.info("Database initialization completed.");
	}

}
