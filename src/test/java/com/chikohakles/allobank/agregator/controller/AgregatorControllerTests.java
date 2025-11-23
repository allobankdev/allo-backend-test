package com.chikohakles.allobank.agregator.controller;

import com.chikohakles.allobank.agregator.service.AgregatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgregatorControllerTest {

    @Mock
    AgregatorService agregatorService;

    @InjectMocks
    AgregatorController controller;

    @Test
    void getData_WhenServiceReturnsData_ShouldReturn200WithBody() {
        String resourceType = "latest_idr_rates";
        Object mockData = "some-data";
        when(agregatorService.getData(resourceType)).thenReturn(mockData);

        ResponseEntity<?> response = controller.getData(resourceType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(mockData);
        verify(agregatorService).getData(resourceType);
    }

    @Test
    void getData_WhenServiceReturnsNull_ShouldReturn404() {
        String resourceType = "latest_idr_rates";
        when(agregatorService.getData(resourceType)).thenReturn(null);

        ResponseEntity<?> response = controller.getData(resourceType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
        assertThat(response.getBody()).isNull();
    }
}

