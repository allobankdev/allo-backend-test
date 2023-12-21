package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.PartaiService;
import com.allobank.allobackendtest.utils.constant.ApiPathConstant;
import com.allobank.allobackendtest.utils.customeResponse.PageResponseWrapper;
import com.allobank.allobackendtest.utils.customeResponse.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.scanner.Constant;

import java.util.List;

@RestController
@RequestMapping(ApiPathConstant.PARTAI_PATH)
public class PartaiController {
    @Autowired
    private PartaiService partaiService;


    @PostMapping
    public ResponseEntity<Response<Partai>> savePartai(@RequestBody Partai partai){
        String message = String.format(ApiPathConstant.MESSAGE, "Partai");
        Response<Partai> response = new Response<>();
        response.setMessage(message);
        response.setData(partaiService.savePartai(partai));
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    @GetMapping
    public PageResponseWrapper<Partai> getAllPartai(@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
                                                    @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                    @RequestParam(name = "sort-by", defaultValue = "nomorUrut")String sortBy,
                                                    @RequestParam(name = "direction", defaultValue = "ASC") String direction){

        Sort sorting = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(pageNumber, size, sorting);
        Page<Partai> page = partaiService.getPartaiPerPage(pageable);
        return new PageResponseWrapper<>(page);
    }
    @PutMapping
    public Partai updatePartai(@RequestBody Partai partai){
        return partaiService.updatePartai(partai);
    }
    @GetMapping("/{id}")
    public Partai getById(@PathVariable String id){
        return partaiService.getPartaiById(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        partaiService.deletePartai(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/search")
    public List<Partai> searchPartai(@RequestParam(name = "namaPartai") String namaPartai){
        return partaiService.searchPartai(namaPartai);
    }


}
