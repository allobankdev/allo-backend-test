package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CalegServiceTest {

    @InjectMocks
    private CalegService calegService;

    @Mock
    private CalegRepository calegRepository;

    @Test
    public void testGetAllCalegs() {
        MockitoAnnotations.openMocks(this);
        
        UUID dapilId = UUID.randomUUID();
        UUID partaiId = UUID.randomUUID();
        
        Caleg caleg = new Caleg();
        caleg.setId(UUID.randomUUID());
        caleg.setNama("Budi");
        caleg.setNomorUrut(1);
        
        when(calegRepository.findByDapilIdAndPartaiId(dapilId, partaiId, PageRequest.of(0, 10)))
                .thenReturn(List.of(caleg));
        
        List<Caleg> calegs = calegService.getAllCalegs(dapilId, partaiId, 0, 10);
        
        assert calegs.size() == 1;
