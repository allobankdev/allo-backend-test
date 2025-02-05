package com.allobank.allobackendtest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.allobank.allobackendtest.entities.Caleg;
import com.allobank.allobackendtest.entities.Dapil;
import com.allobank.allobackendtest.entities.Partai;
import com.allobank.allobackendtest.enums.JenisKelamin;
import com.allobank.allobackendtest.models.CalegRequest;
import com.allobank.allobackendtest.models.DapilRequest;
import com.allobank.allobackendtest.models.PartaiRequest;
import com.allobank.allobackendtest.repositories.CalegRepository;
import com.allobank.allobackendtest.repositories.DapilRepository;
import com.allobank.allobackendtest.repositories.PartaiRepository;
import com.allobank.allobackendtest.services.PemiluService;

@SpringBootTest
public class PemiluServiceTest {
	@Mock
	private CalegRepository calegRepository;

	@Mock
	private DapilRepository dapilRepository;

	@Mock
	private PartaiRepository partaiRepository;

	@InjectMocks
	private PemiluService pemiluService;

	@Test
	public void testCreateDapil() {
		UUID dapilId = UUID.randomUUID();
		DapilRequest request = new DapilRequest("Jakarta 1", "DKI Jakarta", List.of("Jakarta Pusat"), 10);

		Dapil dapil = Dapil.builder().id(dapilId).namaDapil(request.getNamaDapil()).provinsi(request.getProvinsi())
				.wilayahDapilList(request.getWilayahDapilList()).jumlahKursi(request.getJumlahKursi()).build();

		Mockito.when(dapilRepository.save(Mockito.any(Dapil.class))).thenReturn(dapil);

		Dapil savedDapil = pemiluService.createDapil(request);

		Assertions.assertNotNull(savedDapil);
		Assertions.assertEquals("Jakarta 1", savedDapil.getNamaDapil());
	}

	@Test
	public void testCreatePartai() {
		UUID partaiId = UUID.randomUUID();

		PartaiRequest request = new PartaiRequest("Partai Alpha", 1);

		Partai partai = Partai.builder().id(partaiId).namaPartai(request.getNamaPartai())
				.nomorUrut(request.getNomorUrut()).build();

		Mockito.when(partaiRepository.save(Mockito.any(Partai.class))).thenReturn(partai);

		Partai savedPartai = pemiluService.createPartai(request);

		Assertions.assertNotNull(savedPartai);
		Assertions.assertEquals("Partai Alpha", savedPartai.getNamaPartai());
		Assertions.assertEquals(1, savedPartai.getNomorUrut());
	}

	@Test
	public void testCreateCaleg() {
		UUID dapilId = UUID.randomUUID();
		UUID partaiId = UUID.randomUUID();

		Dapil dapil = new Dapil(dapilId, "Jakarta 1", "DKI Jakarta", List.of("Jakarta Pusat"), 10);
		Partai partai = new Partai(partaiId, "Partai Alpha", 1);

		CalegRequest request = new CalegRequest(dapilId, partaiId, 1, "John Doe", JenisKelamin.LAKILAKI);
		Caleg caleg = new Caleg(UUID.randomUUID(), dapil, partai, request.getNomorUrut(), request.getNama(),
				request.getJenisKelamin());

		Mockito.when(dapilRepository.findById(dapilId)).thenReturn(Optional.of(dapil));
		Mockito.when(partaiRepository.findById(partaiId)).thenReturn(Optional.of(partai));
		Mockito.when(calegRepository.save(Mockito.any(Caleg.class))).thenReturn(caleg);

		Caleg savedCaleg = pemiluService.createCaleg(request);

		Assertions.assertNotNull(savedCaleg);
		Assertions.assertEquals("John Doe", savedCaleg.getNama());
		Assertions.assertEquals(JenisKelamin.LAKILAKI, savedCaleg.getJenisKelamin());
	}

	@Test
	public void testGetCalegList() {
		UUID dapilId = UUID.randomUUID();
		UUID partaiId = UUID.randomUUID();

		Dapil dapil = new Dapil(dapilId, "Jakarta 1", "DKI Jakarta", List.of("Jakarta Pusat"), 10);
		Partai partai = new Partai(partaiId, "Partai Alpha", 1);

		List<Caleg> calegList = List.of(
				new Caleg(UUID.randomUUID(), dapil, partai, 1, "John Doe", JenisKelamin.LAKILAKI),
				new Caleg(UUID.randomUUID(), dapil, partai, 2, "Jane Smith", JenisKelamin.PEREMPUAN));

		Mockito.when(calegRepository.findByDapil_IdAndPartai_IdOrderByNomorUrut(dapilId, partaiId))
				.thenReturn(calegList);

		List<Caleg> result = pemiluService.getCalegList(dapilId, partaiId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.size());
		Assertions.assertEquals("John Doe", result.get(0).getNama());
		Assertions.assertEquals(1, result.get(0).getNomorUrut());
		Assertions.assertEquals("Jane Smith", result.get(1).getNama());
		Assertions.assertEquals(2, result.get(1).getNomorUrut());
	}
}
