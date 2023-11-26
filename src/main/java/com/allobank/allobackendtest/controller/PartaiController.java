package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Partai;
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
public class PartaiController {
    private PartaiService partaiService;

    @GetMapping(value = "/partai")
    private ResponseEntity<List<Partai>> findAllPartai(@RequestParam(required = false) String sort){
        List<Partai> partai;
        Sort sortData;
        if(sort == null || sort.isEmpty()){
            partai=partaiService.getAllPartai();
        }else{
            sort = sort.toLowerCase();
            if("asc".equals(sort)) {
                sortData = Sort.by(Sort.Direction.ASC, "nomorUrut");
            }else{
                sortData = Sort.by(Sort.Direction.DESC, "nomorUrut");
            }
            partai=partaiService.getAllPartaiSort(sortData);
        }
        return new ResponseEntity<>(partai, HttpStatus.OK);
    }


    @GetMapping(value = "/partai/{id}")
    private ResponseEntity<Partai> findPartaiById(@PathVariable("id") UUID partaiId){
        Partai dataPartai = partaiService.getPartaiById(partaiId);
        return new ResponseEntity<>(dataPartai, HttpStatus.OK);
    }

    @DeleteMapping(value = "/partai/{id}")
    private ResponseEntity<String> deletePartaiById(@PathVariable("id") UUID partaiId){
        partaiService.deletePartai(partaiId);
        return new ResponseEntity<>("Partai successfully deleted!", HttpStatus.OK);
    }

    @PutMapping(value = "/partai/{id}")
    private ResponseEntity<Partai> updatePartai(@PathVariable("id") UUID partaiId, @RequestBody Partai partai){
        partai.setId(partaiId);
        Partai dataPartai = partaiService.updatePartai(partai);
        return new ResponseEntity<>(dataPartai,HttpStatus.OK);
    }

    @PostMapping(value = "/partai")
    private ResponseEntity<Partai> saveDataPartai(@RequestBody Partai partai){
        Partai insertPartai = partaiService.createPartai(partai);
        return ResponseEntity.ok(insertPartai);
    }
}
