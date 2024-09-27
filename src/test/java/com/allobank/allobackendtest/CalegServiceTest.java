//package com.allobank.allobackendtest;
//
//import com.allobank.allobackendtest.model.Caleg;
//import com.allobank.allobackendtest.repository.CalegRepository;
//import com.allobank.allobackendtest.service.CalegService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.times;
//
//public class CalegServiceTest {
//
//    @InjectMocks
//    private CalegService calegService;
//
//    @Mock
//    private CalegRepository calegRepository;
//
//    private Caleg caleg;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        caleg = new Caleg();
//        caleg.setId(UUID.randomUUID());
//        caleg.setNama("Caleg 1");
//    }
//
//    @Test
//    void testGetAllCaleg_NoFilters() {
//        when(calegRepository.findAll(any(), any())).thenReturn(Collections.singletonList(caleg));
//
//        List<Caleg> result = calegService.getAllCaleg(null, null, "nomorUrut");
//
//        assertEquals(1, result.size());
//        assertEquals("Caleg 1", result.get(0).getNama());
//        verify(calegRepository, times(1)).findAll(any(), any());
//    }
//}
