package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.WebResponse;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class CalegControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CalegRepository calegRepository;

    @Autowired
    private DapilRepository dapilRepository;

    @Autowired
    private PartaiRepository partaiRepository;


    @BeforeEach
    void setUp() {
        calegRepository.deleteAll();
        dapilRepository.deleteAll();
        partaiRepository.deleteAll();
    }


    @Test
    void testSuccessGetAllCaleg() throws Exception {

        PartaiEntity partai1 = new PartaiEntity();
        partai1.setId(UUID.randomUUID());
        partai1.setNamaPartai("Partai 1");
        partai1.setNomorUrut(1);

        PartaiEntity partai2 = new PartaiEntity();
        partai2.setId(UUID.randomUUID());
        partai2.setNamaPartai("Partai 2");
        partai2.setNomorUrut(2);

        partaiRepository.save(partai1);
        partaiRepository.save(partai2);

        DapilEntity dapil1 = new DapilEntity();
        dapil1.setId(UUID.randomUUID());
        dapil1.setNamaDapil("Dapil 1");
        dapil1.setProvinsi("Provinsi 1");
        dapil1.setWilayahDapilList(List.of("Wilayah 1", "Wilayah 2"));
        dapil1.setJumlahKursi(100);

        DapilEntity dapil2 = new DapilEntity();
        dapil2.setId(UUID.randomUUID());
        dapil2.setNamaDapil("Dapil 2");
        dapil2.setProvinsi("Provinsi 2");
        dapil2.setWilayahDapilList(List.of("Wilayah 3", "Wilayah 4"));
        dapil2.setJumlahKursi(200);

        dapilRepository.save(dapil1);
        dapilRepository.save(dapil2);

        CalegEntity caleg1 = new CalegEntity();
        caleg1.setId(UUID.randomUUID());
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);
        caleg1.setDapil(dapil1);
        caleg1.setPartai(partai1);
        caleg1.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg2 = new CalegEntity();
        caleg2.setId(UUID.randomUUID());
        caleg2.setNama("Caleg 2");
        caleg2.setNomorUrut(2);
        caleg2.setDapil(dapil1);
        caleg2.setPartai(partai2);
        caleg2.setJenisKelamin(JenisKelamin.PEREMPUAN);

        CalegEntity caleg3 = new CalegEntity();
        caleg3.setId(UUID.randomUUID());
        caleg3.setNama("Caleg 3");
        caleg3.setNomorUrut(3);
        caleg3.setDapil(dapil2);
        caleg3.setPartai(partai1);
        caleg3.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg4 = new CalegEntity();
        caleg4.setId(UUID.randomUUID());
        caleg4.setNama("Caleg 4");
        caleg4.setNomorUrut(4);
        caleg4.setDapil(dapil2);
        caleg4.setPartai(partai2);
        caleg4.setJenisKelamin(JenisKelamin.PEREMPUAN);

        calegRepository.save(caleg4);
        calegRepository.save(caleg3);
        calegRepository.save(caleg2);
        calegRepository.save(caleg1);


        mockMvc.perform(
                get("/api/caleg")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<Caleg>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            // CEK ERROR IS NULL
            assertNull(response.getErrors());

            // CEK JUMLAH DATA
            assertEquals(4, response.getData().size());

            // CEK ORDER (DEFAULT BY NOMOR_URUT)
            assertEquals(1, response.getData().get(0).getNomorUrut());
            assertEquals(2, response.getData().get(1).getNomorUrut());
            assertEquals(3, response.getData().get(2).getNomorUrut());
            assertEquals(4, response.getData().get(3).getNomorUrut());


            // CEK DATA
            assertEquals("Partai 1", response.getData().get(0).getPartai().getNamaPartai());
            assertEquals("Partai 1", response.getData().get(2).getPartai().getNamaPartai());
            assertEquals("Partai 2", response.getData().get(1).getPartai().getNamaPartai());
            assertEquals("Partai 2", response.getData().get(3).getPartai().getNamaPartai());

            assertEquals("Dapil 1", response.getData().get(0).getDapil().getNamaDapil());
            assertEquals("Dapil 1", response.getData().get(1).getDapil().getNamaDapil());
            assertEquals("Dapil 2", response.getData().get(2).getDapil().getNamaDapil());
            assertEquals("Dapil 2", response.getData().get(3).getDapil().getNamaDapil());

            assertEquals("Caleg 1", response.getData().get(0).getNama());
            assertEquals("Caleg 2", response.getData().get(1).getNama());
            assertEquals("Caleg 3", response.getData().get(2).getNama());
            assertEquals("Caleg 4", response.getData().get(3).getNama());

            assertEquals(1, response.getData().get(0).getNomorUrut());
            assertEquals(2, response.getData().get(1).getNomorUrut());
            assertEquals(3, response.getData().get(2).getNomorUrut());
            assertEquals(4, response.getData().get(3).getNomorUrut());

            assertEquals(JenisKelamin.LAKILAKI, response.getData().get(0).getJenisKelamin());
            assertEquals(JenisKelamin.PEREMPUAN, response.getData().get(1).getJenisKelamin());
            assertEquals(JenisKelamin.LAKILAKI, response.getData().get(2).getJenisKelamin());
            assertEquals(JenisKelamin.PEREMPUAN, response.getData().get(3).getJenisKelamin());

            assertEquals("Wilayah 1", response.getData().get(0).getDapil().getWilayahDapilList().stream().toList().get(0));
            assertEquals("Wilayah 2", response.getData().get(0).getDapil().getWilayahDapilList().stream().toList().get(1));
            assertEquals("Wilayah 1", response.getData().get(1).getDapil().getWilayahDapilList().stream().toList().get(0));
            assertEquals("Wilayah 2", response.getData().get(1).getDapil().getWilayahDapilList().stream().toList().get(1));

            assertEquals("Wilayah 3", response.getData().get(2).getDapil().getWilayahDapilList().stream().toList().get(0));
            assertEquals("Wilayah 4", response.getData().get(3).getDapil().getWilayahDapilList().stream().toList().get(1));
            assertEquals("Wilayah 3", response.getData().get(3).getDapil().getWilayahDapilList().stream().toList().get(0));
            assertEquals("Wilayah 4", response.getData().get(3).getDapil().getWilayahDapilList().stream().toList().get(1));

            assertEquals("Provinsi 1", response.getData().get(0).getDapil().getProvinsi());
            assertEquals("Provinsi 1", response.getData().get(1).getDapil().getProvinsi());
            assertEquals("Provinsi 2", response.getData().get(2).getDapil().getProvinsi());
            assertEquals("Provinsi 2", response.getData().get(3).getDapil().getProvinsi());

            assertEquals(100, response.getData().get(0).getDapil().getJumlahKursi());
            assertEquals(100, response.getData().get(1).getDapil().getJumlahKursi());
            assertEquals(200, response.getData().get(2).getDapil().getJumlahKursi());
            assertEquals(200, response.getData().get(3).getDapil().getJumlahKursi());

        });
    }

    @Test
    void testSuccessGetCalegByDapil() throws Exception{

        PartaiEntity partai1 = new PartaiEntity();
        partai1.setId(UUID.randomUUID());
        partai1.setNamaPartai("Partai 1");
        partai1.setNomorUrut(1);

        PartaiEntity partai2 = new PartaiEntity();
        partai2.setId(UUID.randomUUID());
        partai2.setNamaPartai("Partai 2");
        partai2.setNomorUrut(2);

        partaiRepository.save(partai1);
        partaiRepository.save(partai2);

        DapilEntity dapil1 = new DapilEntity();
        dapil1.setId(UUID.randomUUID());
        dapil1.setNamaDapil("Dapil 1");
        dapil1.setProvinsi("Provinsi 1");
        dapil1.setWilayahDapilList(List.of("Wilayah 1", "Wilayah 2"));
        dapil1.setJumlahKursi(100);

        DapilEntity dapil2 = new DapilEntity();
        dapil2.setId(UUID.randomUUID());
        dapil2.setNamaDapil("Dapil 2");
        dapil2.setProvinsi("Provinsi 2");
        dapil2.setWilayahDapilList(List.of("Wilayah 3", "Wilayah 4"));
        dapil2.setJumlahKursi(200);

        dapilRepository.save(dapil1);
        dapilRepository.save(dapil2);

        CalegEntity caleg1 = new CalegEntity();
        caleg1.setId(UUID.randomUUID());
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);
        caleg1.setDapil(dapil1);
        caleg1.setPartai(partai1);
        caleg1.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg2 = new CalegEntity();
        caleg2.setId(UUID.randomUUID());
        caleg2.setNama("Caleg 2");
        caleg2.setNomorUrut(2);
        caleg2.setDapil(dapil1);
        caleg2.setPartai(partai2);
        caleg2.setJenisKelamin(JenisKelamin.PEREMPUAN);

        CalegEntity caleg3 = new CalegEntity();
        caleg3.setId(UUID.randomUUID());
        caleg3.setNama("Caleg 3");
        caleg3.setNomorUrut(3);
        caleg3.setDapil(dapil2);
        caleg3.setPartai(partai1);
        caleg3.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg4 = new CalegEntity();
        caleg4.setId(UUID.randomUUID());
        caleg4.setNama("Caleg 4");
        caleg4.setNomorUrut(4);
        caleg4.setDapil(dapil2);
        caleg4.setPartai(partai2);
        caleg4.setJenisKelamin(JenisKelamin.PEREMPUAN);

        calegRepository.save(caleg4);
        calegRepository.save(caleg3);
        calegRepository.save(caleg2);
        calegRepository.save(caleg1);

        mockMvc.perform(
                get("/api/caleg?dapil=Dapil 1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<Caleg>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            // CEK ERROR NULL
            assertNull(response.getErrors());
            assertEquals(2, response.getData().size());

            // CEK DATA
            assertEquals("Caleg 1", response.getData().get(0).getNama());
            assertEquals("Caleg 2", response.getData().get(1).getNama());

        });
    }

    @Test
    void testSuccessGetCalegByPartai() throws Exception{

        PartaiEntity partai1 = new PartaiEntity();
        partai1.setId(UUID.randomUUID());
        partai1.setNamaPartai("Partai 1");
        partai1.setNomorUrut(1);

        PartaiEntity partai2 = new PartaiEntity();
        partai2.setId(UUID.randomUUID());
        partai2.setNamaPartai("Partai 2");
        partai2.setNomorUrut(2);

        partaiRepository.save(partai1);
        partaiRepository.save(partai2);

        DapilEntity dapil1 = new DapilEntity();
        dapil1.setId(UUID.randomUUID());
        dapil1.setNamaDapil("Dapil 1");
        dapil1.setProvinsi("Provinsi 1");
        dapil1.setWilayahDapilList(List.of("Wilayah 1", "Wilayah 2"));
        dapil1.setJumlahKursi(100);

        DapilEntity dapil2 = new DapilEntity();
        dapil2.setId(UUID.randomUUID());
        dapil2.setNamaDapil("Dapil 2");
        dapil2.setProvinsi("Provinsi 2");
        dapil2.setWilayahDapilList(List.of("Wilayah 3", "Wilayah 4"));
        dapil2.setJumlahKursi(200);

        dapilRepository.save(dapil1);
        dapilRepository.save(dapil2);

        CalegEntity caleg1 = new CalegEntity();
        caleg1.setId(UUID.randomUUID());
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);
        caleg1.setDapil(dapil1);
        caleg1.setPartai(partai1);
        caleg1.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg2 = new CalegEntity();
        caleg2.setId(UUID.randomUUID());
        caleg2.setNama("Caleg 2");
        caleg2.setNomorUrut(2);
        caleg2.setDapil(dapil1);
        caleg2.setPartai(partai2);
        caleg2.setJenisKelamin(JenisKelamin.PEREMPUAN);

        CalegEntity caleg3 = new CalegEntity();
        caleg3.setId(UUID.randomUUID());
        caleg3.setNama("Caleg 3");
        caleg3.setNomorUrut(3);
        caleg3.setDapil(dapil2);
        caleg3.setPartai(partai1);
        caleg3.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg4 = new CalegEntity();
        caleg4.setId(UUID.randomUUID());
        caleg4.setNama("Caleg 4");
        caleg4.setNomorUrut(4);
        caleg4.setDapil(dapil2);
        caleg4.setPartai(partai2);
        caleg4.setJenisKelamin(JenisKelamin.PEREMPUAN);

        calegRepository.save(caleg4);
        calegRepository.save(caleg3);
        calegRepository.save(caleg2);
        calegRepository.save(caleg1);

        mockMvc.perform(
                get("/api/caleg?partai=Partai 1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<Caleg>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            // CEK ERROR NULL
            assertNull(response.getErrors());
            assertEquals(2, response.getData().size());

            // CEK DATA
            assertEquals("Caleg 1", response.getData().get(0).getNama());
            assertEquals("Caleg 3", response.getData().get(1).getNama());

        });
    }

    @Test
    void testSuccessGetCalegByDapilAndPartai() throws Exception{

        PartaiEntity partai1 = new PartaiEntity();
        partai1.setId(UUID.randomUUID());
        partai1.setNamaPartai("Partai 1");
        partai1.setNomorUrut(1);

        PartaiEntity partai2 = new PartaiEntity();
        partai2.setId(UUID.randomUUID());
        partai2.setNamaPartai("Partai 2");
        partai2.setNomorUrut(2);

        partaiRepository.save(partai1);
        partaiRepository.save(partai2);

        DapilEntity dapil1 = new DapilEntity();
        dapil1.setId(UUID.randomUUID());
        dapil1.setNamaDapil("Dapil 1");
        dapil1.setProvinsi("Provinsi 1");
        dapil1.setWilayahDapilList(List.of("Wilayah 1", "Wilayah 2"));
        dapil1.setJumlahKursi(100);

        DapilEntity dapil2 = new DapilEntity();
        dapil2.setId(UUID.randomUUID());
        dapil2.setNamaDapil("Dapil 2");
        dapil2.setProvinsi("Provinsi 2");
        dapil2.setWilayahDapilList(List.of("Wilayah 3", "Wilayah 4"));
        dapil2.setJumlahKursi(200);

        dapilRepository.save(dapil1);
        dapilRepository.save(dapil2);

        CalegEntity caleg1 = new CalegEntity();
        caleg1.setId(UUID.randomUUID());
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);
        caleg1.setDapil(dapil1);
        caleg1.setPartai(partai1);
        caleg1.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg2 = new CalegEntity();
        caleg2.setId(UUID.randomUUID());
        caleg2.setNama("Caleg 2");
        caleg2.setNomorUrut(2);
        caleg2.setDapil(dapil1);
        caleg2.setPartai(partai2);
        caleg2.setJenisKelamin(JenisKelamin.PEREMPUAN);

        CalegEntity caleg3 = new CalegEntity();
        caleg3.setId(UUID.randomUUID());
        caleg3.setNama("Caleg 3");
        caleg3.setNomorUrut(3);
        caleg3.setDapil(dapil2);
        caleg3.setPartai(partai1);
        caleg3.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg4 = new CalegEntity();
        caleg4.setId(UUID.randomUUID());
        caleg4.setNama("Caleg 4");
        caleg4.setNomorUrut(4);
        caleg4.setDapil(dapil2);
        caleg4.setPartai(partai2);
        caleg4.setJenisKelamin(JenisKelamin.PEREMPUAN);

        calegRepository.save(caleg4);
        calegRepository.save(caleg3);
        calegRepository.save(caleg2);
        calegRepository.save(caleg1);

        mockMvc.perform(
                get("/api/caleg?partai=Partai 1&dapil=Dapil 1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<Caleg>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            // CEK ERROR NULL
            assertNull(response.getErrors());
            assertEquals(1, response.getData().size());

            // CEK DATA
            assertEquals("Caleg 1", response.getData().get(0).getNama());
        });
    }


    @Test
    void testFailedGetCalegByPartai() throws Exception{

        PartaiEntity partai1 = new PartaiEntity();
        partai1.setId(UUID.randomUUID());
        partai1.setNamaPartai("Partai 1");
        partai1.setNomorUrut(1);

        PartaiEntity partai2 = new PartaiEntity();
        partai2.setId(UUID.randomUUID());
        partai2.setNamaPartai("Partai 2");
        partai2.setNomorUrut(2);

        partaiRepository.save(partai1);
        partaiRepository.save(partai2);

        DapilEntity dapil1 = new DapilEntity();
        dapil1.setId(UUID.randomUUID());
        dapil1.setNamaDapil("Dapil 1");
        dapil1.setProvinsi("Provinsi 1");
        dapil1.setWilayahDapilList(List.of("Wilayah 1", "Wilayah 2"));
        dapil1.setJumlahKursi(100);

        DapilEntity dapil2 = new DapilEntity();
        dapil2.setId(UUID.randomUUID());
        dapil2.setNamaDapil("Dapil 2");
        dapil2.setProvinsi("Provinsi 2");
        dapil2.setWilayahDapilList(List.of("Wilayah 3", "Wilayah 4"));
        dapil2.setJumlahKursi(200);

        dapilRepository.save(dapil1);
        dapilRepository.save(dapil2);

        mockMvc.perform(
                get("/api/caleg?partai=Invalid Partai")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    void testFailedGetCalegByDapil() throws Exception{

        PartaiEntity partai1 = new PartaiEntity();
        partai1.setId(UUID.randomUUID());
        partai1.setNamaPartai("Partai 1");
        partai1.setNomorUrut(1);

        PartaiEntity partai2 = new PartaiEntity();
        partai2.setId(UUID.randomUUID());
        partai2.setNamaPartai("Partai 2");
        partai2.setNomorUrut(2);

        partaiRepository.save(partai1);
        partaiRepository.save(partai2);

        DapilEntity dapil1 = new DapilEntity();
        dapil1.setId(UUID.randomUUID());
        dapil1.setNamaDapil("Dapil 1");
        dapil1.setProvinsi("Provinsi 1");
        dapil1.setWilayahDapilList(List.of("Wilayah 1", "Wilayah 2"));
        dapil1.setJumlahKursi(100);

        DapilEntity dapil2 = new DapilEntity();
        dapil2.setId(UUID.randomUUID());
        dapil2.setNamaDapil("Dapil 2");
        dapil2.setProvinsi("Provinsi 2");
        dapil2.setWilayahDapilList(List.of("Wilayah 3", "Wilayah 4"));
        dapil2.setJumlahKursi(200);

        dapilRepository.save(dapil1);
        dapilRepository.save(dapil2);

        mockMvc.perform(
                get("/api/caleg?dapil=Invalid Dapil")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isBadRequest()
        );
    }


    @Test
    void testFailedGetCalegNotFound() throws Exception{

        PartaiEntity partai1 = new PartaiEntity();
        partai1.setId(UUID.randomUUID());
        partai1.setNamaPartai("Partai 1");
        partai1.setNomorUrut(1);

        PartaiEntity partai2 = new PartaiEntity();
        partai2.setId(UUID.randomUUID());
        partai2.setNamaPartai("Partai 2");
        partai2.setNomorUrut(2);

        partaiRepository.save(partai1);
        partaiRepository.save(partai2);

        DapilEntity dapil1 = new DapilEntity();
        dapil1.setId(UUID.randomUUID());
        dapil1.setNamaDapil("Dapil 1");
        dapil1.setProvinsi("Provinsi 1");
        dapil1.setWilayahDapilList(List.of("Wilayah 1", "Wilayah 2"));
        dapil1.setJumlahKursi(100);

        DapilEntity dapil2 = new DapilEntity();
        dapil2.setId(UUID.randomUUID());
        dapil2.setNamaDapil("Dapil 2");
        dapil2.setProvinsi("Provinsi 2");
        dapil2.setWilayahDapilList(List.of("Wilayah 3", "Wilayah 4"));
        dapil2.setJumlahKursi(200);

        dapilRepository.save(dapil1);
        dapilRepository.save(dapil2);

        CalegEntity caleg1 = new CalegEntity();
        caleg1.setId(UUID.randomUUID());
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);
        caleg1.setDapil(dapil1);
        caleg1.setPartai(partai1);
        caleg1.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg2 = new CalegEntity();
        caleg2.setId(UUID.randomUUID());
        caleg2.setNama("Caleg 2");
        caleg2.setNomorUrut(2);
        caleg2.setDapil(dapil1);
        caleg2.setPartai(partai2);
        caleg2.setJenisKelamin(JenisKelamin.PEREMPUAN);

        CalegEntity caleg3 = new CalegEntity();
        caleg3.setId(UUID.randomUUID());
        caleg3.setNama("Caleg 3");
        caleg3.setNomorUrut(3);
        caleg3.setDapil(dapil2);
        caleg3.setPartai(partai1);
        caleg3.setJenisKelamin(JenisKelamin.LAKILAKI);

        CalegEntity caleg4 = new CalegEntity();
        caleg4.setId(UUID.randomUUID());
        caleg4.setNama("Caleg 4");
        caleg4.setNomorUrut(4);
        caleg4.setDapil(dapil2);
        caleg4.setPartai(partai2);
        caleg4.setJenisKelamin(JenisKelamin.PEREMPUAN);

        mockMvc.perform(
                get("/api/caleg?dapil=Dapil 1&partai=Partai 1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isNotFound()
        );
    }
}