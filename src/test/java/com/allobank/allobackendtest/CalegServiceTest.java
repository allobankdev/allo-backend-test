package com.allobank.allobackendtest;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.service.CalegService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CalegServiceTest {

    @Mock
    private CalegRepository calegRepository;

    @InjectMocks
    private CalegService calegService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCaleg() {
        // Mocking the repository behavior
        List<Caleg> mockCalegs = Arrays.asList(new Caleg(), new Caleg());
        when(calegRepository.findAll()).thenReturn(mockCalegs);

        // Testing the service method
        List<Caleg> result = calegService.getAllCaleg();

        // Verifying the result
        assertEquals(2, result.size());
        verify(calegRepository, times(1)).findAll();
        verifyNoMoreInteractions(calegRepository);
    }

    @Test
    public void testGetCalegList() {
        // Mocking the repository behavior
        List<Caleg> mockCalegs = Arrays.asList(new Caleg(), new Caleg());
        when(calegRepository.findByDapilIdAndPartaiId(any(), any(), any())).thenReturn(mockCalegs);

        // Testing the service method
        List<Caleg> result = calegService.getCalegList("dapilId", "partaiId", "nomorUrut", "ASC");

        // Verifying the result
        assertEquals(2, result.size());
        verify(calegRepository, times(1)).findByDapilIdAndPartaiId(any(), any(), any());
        verifyNoMoreInteractions(calegRepository);
    }

    @Test
    public void testGetCalegById() {
        // Mocking the repository behavior
        String calegId = "1";
        Caleg mockCaleg = new Caleg();
        when(calegRepository.findById(calegId)).thenReturn(Optional.of(mockCaleg));

        // Testing the service method
        Caleg result = calegService.getCalegById(calegId);

        // Verifying the result
        assertEquals(mockCaleg, result);
        verify(calegRepository, times(1)).findById(calegId);
        verifyNoMoreInteractions(calegRepository);
    }

    @Test
    public void testCreateCaleg() {
        // Mocking the repository behavior
        Caleg mockCaleg = new Caleg();
        when(calegRepository.save(any())).thenReturn(mockCaleg);

        // Testing the service method
        Caleg result = calegService.createCaleg(new Caleg());

        // Verifying the result
        assertEquals(mockCaleg, result);
        verify(calegRepository, times(1)).save(any());
        verifyNoMoreInteractions(calegRepository);
    }

    @Test
    public void testUpdateCaleg() {
        // Mocking the repository behavior
        String calegId = "1";
        Caleg mockCaleg = new Caleg();
        when(calegRepository.existsById(calegId)).thenReturn(true);
        when(calegRepository.save(any())).thenReturn(mockCaleg);

        // Testing the service method
        Caleg result = calegService.updateCaleg(calegId, new Caleg());

        // Verifying the result
        assertEquals(mockCaleg, result);
        verify(calegRepository, times(1)).existsById(calegId);
        verify(calegRepository, times(1)).save(any());
        verifyNoMoreInteractions(calegRepository);
    }

    @Test
    public void testUpdateCaleg_NotFound() {
        // Mocking the repository behavior
        String calegId = "1";
        when(calegRepository.existsById(calegId)).thenReturn(false);

        // Testing the service method
        Caleg result = calegService.updateCaleg(calegId, new Caleg());

        // Verifying the result
        assertEquals(null, result);
        verify(calegRepository, times(1)).existsById(calegId);
        verifyNoMoreInteractions(calegRepository);
    }

    @Test
    public void testDeleteCaleg() {
        // Testing the service method
        calegService.deleteCaleg("1");

        // Verifying the result
        verify(calegRepository, times(1)).deleteById("1");
        verifyNoMoreInteractions(calegRepository);
    }
}
