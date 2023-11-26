package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.service.DapilService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/")
@AllArgsConstructor
public class DapilController {

    private DapilService dapilService;

    @GetMapping(value = "/dapil")
    private ResponseEntity<List<Dapil>> findAllDapil(@RequestParam(required = false) String sort){
        List<Dapil> dapil;
        Sort sortData;
        if(sort == null || sort.isEmpty()){
            dapil=dapilService.getAllDapil();
        }else{
            sort = sort.toLowerCase();
            if("asc".equals(sort)) {
                sortData = Sort.by(Sort.Direction.ASC, "jumlahKursi");
            }else{
                sortData = Sort.by(Sort.Direction.DESC, "jumlahKursi");
            }
            dapil=dapilService.getAllDapilSort(sortData);
        }
        return new ResponseEntity<>(dapil, HttpStatus.OK);
    }


    @GetMapping(value = "/dapil/{id}")
    private ResponseEntity<Dapil> findDapilById(@PathVariable("id") UUID dapilId){
        Dapil dataDapil = dapilService.getDapilById(dapilId);
        return new ResponseEntity<>(dataDapil, HttpStatus.OK);
    }

    @DeleteMapping(value = "/dapil/{id}")
    private ResponseEntity<String> deleteDapilById(@PathVariable("id") UUID dapilId){
        dapilService.deleteDapil(dapilId);
        return new ResponseEntity<>("Dapil successfully deleted!", HttpStatus.OK);
    }

    @PutMapping(value = "/dapil/{id}")
    private ResponseEntity<Dapil> updateDapil(@PathVariable("id") UUID dapilId, @RequestBody Dapil dapil){
        dapil.setId(dapilId);
        Dapil dataDapil = dapilService.updateDapil(dapil);
        return new ResponseEntity<>(dataDapil,HttpStatus.OK);
    }

    @PostMapping(value = "/dapil")
    private ResponseEntity<Dapil> saveDataDapil(@RequestBody Dapil dapil){
        Dapil insertDapil = dapilService.createDapil(dapil);
        return ResponseEntity.ok(insertDapil);
    }
}
