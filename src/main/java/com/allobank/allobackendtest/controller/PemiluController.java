package com.allobank.allobackendtest.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.service.PemiluService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class PemiluController {
    
    @Autowired
    private PemiluService pemiluService;

    @GetMapping("/getListCaleg")
    public ResponseEntity<Map<String, Object>> getListCaleg(@RequestParam Map<String, Object> param) throws Exception{
        Map<String, Object> response = new HashMap<>();
		
		try {
			response = pemiluService.getListCaleg(param);
			response.put("status", true);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
			
		}catch (Exception e) {
			response.put("status", e.getMessage());
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
