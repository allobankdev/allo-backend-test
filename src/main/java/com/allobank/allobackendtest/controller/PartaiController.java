package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.PartaiDto;
import com.allobank.allobackendtest.services.PartaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/partai/")
public class PartaiController {

    @Autowired
    PartaiService partaiService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<?> addPartai(@RequestBody PartaiDto partaiDto){

        try {
            partaiService.addPartaiSvc(partaiDto);

        }catch (Exception e){
            e.getStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
