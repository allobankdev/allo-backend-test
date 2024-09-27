package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.DapilDto;
import com.allobank.allobackendtest.services.DapilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dapil/")
public class DapilController {

    @Autowired
    DapilService dapilService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<?> addDapil(@RequestBody DapilDto dapilDto){
        try{
            dapilService.addDapilSvc(dapilDto);
        }catch (Exception e){
            e.getStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
