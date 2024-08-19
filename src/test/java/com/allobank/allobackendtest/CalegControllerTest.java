package com.allobank.allobackendtest;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.services.CalegService;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public class CalegControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private CalegService calegService;

    @MockBean
    private DapilRepository dapilRepository;

    @MockBean
    private PartaiRepository partaiRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllCaleg() throws Exception {
        // Setup dummy data Caleg
        Caleg caleg = new Caleg();
        caleg.setId("caleg1");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Caleg> calegPage = new PageImpl<>(Collections.singletonList(caleg), pageable, 1);

        // Mock behavior dari service
        when(calegService.getAllCaleg(pageable)).thenReturn(calegPage);

        // Perform request ke endpoint
        mockMvc.perform(get("/api/caleg/list")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"content\":[{\"id\":\"caleg1\"}],\"totalElements\":1}"))
                .andDo(print());
    }

    @Test
    public void testGetCalegByDapilAndPartaiWithSorting() throws Exception {
        // Setup dummy data Dapil dan Partai
        Dapil dapil = new Dapil();
        dapil.setId("123e4567-e89b-12d3-a456-426614174113");

        Partai partai = new Partai();
        partai.setId("123e4567-e89b-12d3-a456-426614174222");

        // Setup dua contoh data Caleg dengan nomor urut yang berbeda
        Caleg caleg1 = new Caleg();
        caleg1.setId("caleg1");
        caleg1.setNomorUrut(10);

        Caleg caleg2 = new Caleg();
        caleg2.setId("caleg2");
        caleg2.setNomorUrut(5);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "nomorUrut"));
        Page<Caleg> calegPage = new PageImpl<>(Arrays.asList(caleg1, caleg2), pageable, 2);

        // Mock repository dan service behavior
        when(dapilRepository.findById("123e4567-e89b-12d3-a456-426614174113")).thenReturn(Optional.of(dapil));
        when(partaiRepository.findById("123e4567-e89b-12d3-a456-426614174222")).thenReturn(Optional.of(partai));
        when(calegService.getCalegByDapilAndPartai(dapil, partai, pageable)).thenReturn(calegPage);

        // Perform request ke endpoint dengan parameter dapil_id, partai_id, size, dan
        // sort
        mockMvc.perform(get("/api/caleg/list")
                .param("dapil_id", "123e4567-e89b-12d3-a456-426614174113")
                .param("partai_id", "123e4567-e89b-12d3-a456-426614174222")
                .param("size", "10")
                .param("sort", "nomorUrut,desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{\"content\":[{\"id\":\"caleg1\",\"nomorUrut\":10},{\"id\":\"caleg2\",\"nomorUrut\":5}],\"totalElements\":2}"))
                .andDo(print());
    }

}