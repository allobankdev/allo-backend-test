package com.allobank.allobackendtest.controllertest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.allobank.allobackendtest.controller.CalegController;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.service.CalegService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;

@WebMvcTest(CalegController.class)
public class CalegControllerTest {

   @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalegService calegService; 

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCalegs() throws Exception {
        UUID dapilId = UUID.fromString("530fb181-b0a4-46f5-b61d-ac418999550f");
        UUID partaiId = UUID.fromString("8d2e577a-0eb2-489a-8da9-85561f408614");
        UUID calegId = UUID.randomUUID();

      
        Caleg caleg = new Caleg();
        caleg.setId(calegId);
        caleg.setNama("Budi Santoso");
        caleg.setNomorUrut(1);
        caleg.setDapilId(dapilId);
        caleg.setPartaiId(partaiId);
        caleg.setJenisKelamin(JenisKelamin.LAKILAKI);

        when(calegService.getCalegs(dapilId, partaiId, "nomorUrut"))
            .thenReturn(Collections.singletonList(caleg));

       
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/caleg")
                        .param("dapilId", dapilId.toString())
                        .param("partaiId", partaiId.toString())
                        .param("sortBy", "nomorUrut")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(calegId.toString()))
                .andExpect(jsonPath("$[0].dapilId").value(dapilId.toString()))
                .andExpect(jsonPath("$[0].partaiId").value(partaiId.toString()))
                .andExpect(jsonPath("$[0].nomorUrut").value(1))
                .andExpect(jsonPath("$[0].nama").value("Budi Santoso"))
                .andExpect(jsonPath("$[0].jenisKelamin").value("LAKILAKI"));
    }

    @Test
    public void testCreateCaleg() throws Exception {
        
        UUID dapilId = UUID.fromString("530fb181-b0a4-46f5-b61d-ac418999550f");
        UUID partaiId = UUID.fromString("8d2e577a-0eb2-489a-8da9-85561f408614");
        UUID calegId = UUID.randomUUID();
        
        Caleg inputCaleg = new Caleg();
        inputCaleg.setDapilId(dapilId);
        inputCaleg.setPartaiId(partaiId);
        inputCaleg.setNomorUrut(1);
        inputCaleg.setNama("Eko Santoso");
        inputCaleg.setJenisKelamin(JenisKelamin.LAKILAKI);

        Caleg createdCaleg = new Caleg();
        createdCaleg.setId(calegId);  
        createdCaleg.setDapilId(dapilId);
        createdCaleg.setPartaiId(partaiId);
        createdCaleg.setNomorUrut(1);
        createdCaleg.setNama("Eko Santoso");
        createdCaleg.setJenisKelamin(JenisKelamin.LAKILAKI);

        when(calegService.createCaleg(inputCaleg)).thenReturn(createdCaleg);
        
        mockMvc.perform(post("/api/v1/caleg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputCaleg)))  
                .andExpect(status().isCreated())  
                .andExpect(jsonPath("$.id", is(calegId.toString())))
                .andExpect(jsonPath("$.dapilId", is(dapilId.toString())))
                .andExpect(jsonPath("$.partaiId", is(partaiId.toString())))
                .andExpect(jsonPath("$.nomorUrut", is(1)))
                .andExpect(jsonPath("$.nama", is("Eko Santoso")))
                .andExpect(jsonPath("$.jenisKelamin", is("LAKILAKI")));
    }
}