package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.CalegService;
import com.allobank.allobackendtest.service.DapilService;
import com.allobank.allobackendtest.service.PartaiService;
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
public class CalegController {
    private CalegService calegService;
    private PartaiService partaiService;
    private DapilService dapilService;

    @GetMapping(value = "/caleg")
    private ResponseEntity<List<Caleg>> findAllCaleg(@RequestParam(required = false) String sort){
        List<Caleg> caleg;
        Sort sortData;
        if(sort == null){
            caleg=calegService.getAllCaleg();
        }else{
            sort = sort.toLowerCase();
            if("asc".equals(sort)) {
                sortData = Sort.by(Sort.Direction.ASC, "nomorUrut");
            }else{
                sortData = Sort.by(Sort.Direction.DESC, "nomorUrut");
            }
            caleg=calegService.getAllCalegSort(sortData);
        }
        return new ResponseEntity<>(caleg, HttpStatus.OK);
    }


    @GetMapping(value = "/caleg/{id}")
    private ResponseEntity<Caleg> findCalegById(@PathVariable("id") UUID calegId){
        Caleg dataCaleg = calegService.getCalegById(calegId);
        return new ResponseEntity<>(dataCaleg, HttpStatus.OK);
    }

    @DeleteMapping(value = "/caleg/{id}")
    private ResponseEntity<String> deleteCalegById(@PathVariable("id") UUID calegId){
        calegService.deleteCaleg(calegId);
        return new ResponseEntity<>("Caleg successfully deleted!", HttpStatus.OK);
    }

    @PutMapping(value = "/caleg/{id}")
    private ResponseEntity<Caleg> updateCaleg(@PathVariable("id") UUID calegId, @RequestBody Caleg caleg){
        caleg.setId(calegId);
        Caleg dataCaleg = calegService.updateCaleg(caleg);
        return new ResponseEntity<>(dataCaleg,HttpStatus.OK);
    }

    @PostMapping(value = "/caleg")
    private ResponseEntity<Caleg> saveDataCaleg(@RequestBody Caleg caleg){
        Caleg insertCaleg = calegService.createCaleg(caleg);
        return ResponseEntity.ok(insertCaleg);
    }

    @GetMapping(value = "/caleg/partai/{partaiId}")
    public ResponseEntity<List<Caleg>> findCalegByPartai(@PathVariable("partaiId") UUID partaiId) {
        Partai partai = partaiService.getPartaiById(partaiId);
        List<Caleg> calegList = calegService.findByPartai(partai);
        return new ResponseEntity<>(calegList, HttpStatus.OK);
    }

    @GetMapping(value = "/caleg/dapil/{dapilId}")
    public ResponseEntity<List<Caleg>> findCalegByDapil(@PathVariable("dapilId") UUID dapilId) {
        Dapil dapil = dapilService.getDapilById(dapilId);
        List<Caleg> calegList = calegService.findByDapil(dapil);
        return new ResponseEntity<>(calegList, HttpStatus.OK);
    }
}
