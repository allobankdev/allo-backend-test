package com.allobank.allobackendtest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.common.Response;
import com.allobank.allobackendtest.dto.CalegFilterDTO;
import com.allobank.allobackendtest.entity.Caleg;
import com.allobank.allobackendtest.service.CalegService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/caleg")
public class CalegController {

    @Autowired
    private CalegService calegService;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllCaleg(@RequestBody(required = false) CalegFilterDTO filterDTO) {
        try {
            List<Caleg> listData = calegService.getAllCaleg(filterDTO);
			
			Response response = new Response();
			response.setStatus("1");
			response.setMessage("OK");
			response.setData(listData);
			
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            e.printStackTrace();
			Response response = new Response();
			response.setStatus("0");
			response.setMessage("ERROR : " + e.getMessage());
			response.setData(null);
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }
}

