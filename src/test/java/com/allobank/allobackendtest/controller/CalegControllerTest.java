package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.CalegResponse;
import com.allobank.allobackendtest.dto.WebResponse;
import com.allobank.allobackendtest.model.*;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.repository.WilayahDapilRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class CalegControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CalegRepository calegRepository;

    @Autowired
    private DapilRepository dapilRepository;

    @Autowired
    private PartaiRepository partaiRepository;

    @Autowired
    private WilayahDapilRepository wilayahDapilRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp(){
        partaiRepository.deleteAll();
        dapilRepository.deleteAll();
        wilayahDapilRepository.deleteAll();
        calegRepository.deleteAll();


        Partai gerindra = new Partai();
        gerindra.setId(UUID.randomUUID());
        gerindra.setNamaPartai("Gerindra");
        gerindra.setNomorUrut(1);

        Partai pdip = new Partai();
        pdip.setId(UUID.randomUUID());
        pdip.setNamaPartai("PDIP");
        pdip.setNomorUrut(2);

        Partai golkar = new Partai();
        golkar.setId(UUID.randomUUID());
        golkar.setNamaPartai("Golkar");
        golkar.setNomorUrut(3);

        List<Partai> partaiList = partaiRepository.saveAll(Arrays.asList(gerindra, pdip, golkar));



        Dapil dki = new Dapil();
        dki.setId(UUID.randomUUID());
        dki.setNamaDapil("DKI Jakarta I");
        dki.setProvinsi("DKI Jakarta");
        dki.setJumlahKursi(100);

        Dapil jatim = new Dapil();
        jatim.setId(UUID.randomUUID());
        jatim.setNamaDapil("Jawa Timur I");
        jatim.setProvinsi("Jawa Timur");
        jatim.setJumlahKursi(100);

        Dapil jabar = new Dapil();
        jabar.setId(UUID.randomUUID());
        jabar.setNamaDapil("Jawa Barat I");
        jabar.setProvinsi("Jawa Barat");
        jabar.setJumlahKursi(100);

        List<Dapil> dapilList = dapilRepository.saveAll(Arrays.asList(dki, jatim, jabar));
        String[][] wilayah = {
                {"Kebon Jeruk", "Palmerah", "Srengseng"},
                {"Banyuwangi", "Blitar", "Bojonegoro"},
                {"Bandung", "Bekasi", "Bogor"}
        };

        for(int i=0; i<dapilList.size(); i++){
            Dapil currentDapil = dapilList.get(i);
            for(String namaWilayah: wilayah[i]){
                WilayahDapil wilayahDapil = new WilayahDapil();
                wilayahDapil.setId(UUID.randomUUID());
                wilayahDapil.setWilayahDapil(namaWilayah);
                wilayahDapil.setDapil(currentDapil);
                wilayahDapilRepository.save(wilayahDapil);
            }
        }



        String[][][] dataCaleg = {
                {
                        {"1", "Budi", JenisKelamin.LAKILAKI.toString()},
                        {"2", "Andi", JenisKelamin.LAKILAKI.toString()},
                        {"3", "Joko", JenisKelamin.LAKILAKI.toString()}
                },
                {
                        {"1", "Puan", JenisKelamin.PEREMPUAN.toString()},
                        {"2", "Dedi", JenisKelamin.LAKILAKI.toString()},
                        {"3", "Sri", JenisKelamin.PEREMPUAN.toString()}
                },
                {
                        {"1", "Tom", JenisKelamin.LAKILAKI.toString()},
                        {"2", "Mega", JenisKelamin.PEREMPUAN.toString()},
                        {"3", "Habibi", JenisKelamin.LAKILAKI.toString()}
                }
        };

        for(int i=0; i<partaiList.size(); i++){
            Partai currentPartai = partaiList.get(i);
            for(int j=0; j<dapilList.size(); j++){
                Dapil currentDapil = dapilList.get(j);

                Caleg caleg = new Caleg();
                caleg.setId(UUID.randomUUID());
                caleg.setNomorUrut(Integer.parseInt(dataCaleg[i][j][0]));
                caleg.setNama(dataCaleg[i][j][1]);
                caleg.setJenisKelamin(JenisKelamin.valueOf(dataCaleg[i][j][2]));
                caleg.setPartai(currentPartai);
                caleg.setDapil(currentDapil);
                calegRepository.save(caleg);
            }
        }

    }



    @Test
    void getCalegWithoutQueryParam() throws Exception{
        mockMvc.perform(
                get("/v1/api/caleg")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<CalegResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<CalegResponse>>>() {
            });

            assertEquals(5, response.data().size());
            assertEquals(2, response.paging().totalPage());
            assertEquals(0, response.paging().currentPage());
            assertEquals(5, response.paging().size());


            assertEquals(1, response.data().get(0).nomorUrut());
            assertEquals("Puan", response.data().get(0).nama());
            assertEquals(JenisKelamin.PEREMPUAN, response.data().get(0).jenisKelamin());

            assertEquals(1, response.data().get(1).nomorUrut());
            assertEquals("Budi", response.data().get(1).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(1).jenisKelamin());

            assertEquals(1, response.data().get(2).nomorUrut());
            assertEquals("Tom", response.data().get(2).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(2).jenisKelamin());

            assertEquals(2, response.data().get(3).nomorUrut());
            assertEquals("Dedi", response.data().get(3).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(3).jenisKelamin());

            assertEquals(2, response.data().get(4).nomorUrut());
            assertEquals("Andi", response.data().get(4).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(4).jenisKelamin());

        });
    }


    @Test
    void getCalegNotFound() throws Exception{
        mockMvc.perform(
                get("/v1/api/caleg")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("partai", "PSI")
                        .param("dapil", "Timor Leste")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<CalegResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<CalegResponse>>>() {
            });

            assertEquals(0, response.data().size());
            assertEquals(0, response.paging().totalPage());
            assertEquals(0, response.paging().currentPage());
            assertEquals(5, response.paging().size());

        });
    }



    @Test
    void getCalegWithDapilQueryParam() throws Exception{
        mockMvc.perform(
                get("/v1/api/caleg")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("dapil", "DKI Jakarta I")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<CalegResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<CalegResponse>>>() {
            });

            assertEquals(3, response.data().size());
            assertEquals(1, response.paging().totalPage());
            assertEquals(0, response.paging().currentPage());
            assertEquals(5, response.paging().size());


            assertEquals(1, response.data().get(0).nomorUrut());
            assertEquals("Budi", response.data().get(0).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(0).jenisKelamin());

            assertEquals(1, response.data().get(1).nomorUrut());
            assertEquals("Puan", response.data().get(1).nama());
            assertEquals(JenisKelamin.PEREMPUAN, response.data().get(1).jenisKelamin());

            assertEquals(1, response.data().get(2).nomorUrut());
            assertEquals("Tom", response.data().get(2).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(2).jenisKelamin());

        });
    }


    @Test
    void getCalegWithpPartaiQueryParam() throws Exception{
        mockMvc.perform(
                get("/v1/api/caleg")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("partai", "Gerindra")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<CalegResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<CalegResponse>>>() {
            });

            assertEquals(3, response.data().size());
            assertEquals(1, response.paging().totalPage());
            assertEquals(0, response.paging().currentPage());
            assertEquals(5, response.paging().size());


            assertEquals(1, response.data().get(0).nomorUrut());
            assertEquals("Budi", response.data().get(0).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(0).jenisKelamin());

            assertEquals(2, response.data().get(1).nomorUrut());
            assertEquals("Andi", response.data().get(1).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(1).jenisKelamin());

            assertEquals(3, response.data().get(2).nomorUrut());
            assertEquals("Joko", response.data().get(2).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(2).jenisKelamin());

        });
    }


    @Test
    void getCalegWithpPartaiAndDapilQueryParam() throws Exception{
        mockMvc.perform(
                get("/v1/api/caleg")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("partai", "Gerindra")
                        .param("dapil", "DKI Jakarta I")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<CalegResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<CalegResponse>>>() {
            });

            assertEquals(1, response.data().size());
            assertEquals(1, response.paging().totalPage());
            assertEquals(0, response.paging().currentPage());
            assertEquals(5, response.paging().size());


            assertEquals(1, response.data().get(0).nomorUrut());
            assertEquals("Budi", response.data().get(0).nama());
            assertEquals(JenisKelamin.LAKILAKI, response.data().get(0).jenisKelamin());
        });
    }



    @Test
    void getCalegBadRequestMismatchType() throws Exception{
        mockMvc.perform(
                get("/v1/api/caleg")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "a")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.error());
        });
    }

    @Test
    void getCalegBadRequestPropertyReference() throws Exception{
        mockMvc.perform(
                get("/v1/api/caleg")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("sortBy", "gaji")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.error());
        });
    }
}