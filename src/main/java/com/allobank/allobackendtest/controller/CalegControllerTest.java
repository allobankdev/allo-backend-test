package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.Response;
import com.allobank.allobackendtest.exception.GlobalException;
import com.allobank.allobackendtest.service.CalegServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CalegControllerTest {

    @InjectMocks
    private CalegController calegController;

    @Mock
    private CalegServiceInterface calegService;

    @Mock
    private HttpServletRequest httpreq;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAll() throws GlobalException {
        String dapil = null;
        String partai = null;
        String sortBy = null;

        Response response = new Response();
        response.setRc("000");
        response.setMessage("Success");
        response.setData(Collections.emptyList());

        when(calegService.listAllCaleg(dapil, partai, sortBy)).thenReturn(response);

        ResponseEntity<Response> result = calegController.listAll(httpreq, dapil, partai, sortBy);

        System.out.println("Response Code: " + result.getBody().getRc());
        System.out.println("Response Message: " + result.getBody().getMessage());
        System.out.println("Response Data: " + result.getBody().getData());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testListAllFilter() throws GlobalException {
        String dapil = "Dapil 1";
        String partai = "Partai A";
        String sortBy = "nomorUrut";

        Response response = new Response();
        response.setRc("000");
        response.setMessage("Success");
        response.setData(Collections.emptyList());

        when(calegService.listAllCaleg(dapil, partai, sortBy)).thenReturn(response);

        ResponseEntity<Response> result = calegController.listAll(httpreq, dapil, partai, sortBy);

        System.out.println("Response Code: " + result.getBody().getRc());
        System.out.println("Response Message: " + result.getBody().getMessage());
        System.out.println("Response Data: " + result.getBody().getData());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testListAllFilterInvalid() throws GlobalException {
        String dapil = "Dapil 1";
        String partai = "Partai A";
        String sortBy = "invalidSort";

        when(calegService.listAllCaleg(dapil, partai, sortBy)).thenThrow(new GlobalException("001", HttpStatus.BAD_GATEWAY));

        GlobalException exception = assertThrows(GlobalException.class, () -> {
            calegController.listAll(httpreq, dapil, partai, sortBy);
        });

        System.out.println("Response Code: 001");
        System.out.println("Response Message: Filter Value Tidak Sesuai");

        assertEquals("001", exception.getCode());
    }
}
