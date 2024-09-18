package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.common.Response;
import com.allobank.allobackendtest.entity.Dapil;
import com.allobank.allobackendtest.service.DapilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("/api/dapil")
public class DapilController {

    @Autowired
    private DapilService dapilService;

    @PostMapping("/create")
    public ResponseEntity<Response> createDapil(@RequestBody Dapil dapil) {
        try {
            Dapil savedDapil = dapilService.saveDapil(dapil);
            Response response = new Response();
            response.setStatus("1");
            response.setMessage("Dapil created successfully");
            response.setData(savedDapil);

            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Response response = new Response();
            response.setStatus("0");
            response.setMessage("ERROR : " + e.getMessage());
            response.setData(null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getDapilById(@PathVariable Long id) {
        try {
            Dapil dapil = dapilService.getDapilById(id);
            Response response = new Response();
            response.setStatus("1");
            response.setMessage("OK");
            response.setData(dapil);

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

    @GetMapping("/all")
    public ResponseEntity<Response> getAllDapils() {
        try {
            List<Dapil> dapils = dapilService.getAllDapils();
            Response response = new Response();
            response.setStatus("1");
            response.setMessage("OK");
            response.setData(dapils);

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteDapil(@PathVariable Long id) {
        try {
            dapilService.deleteDapil(id);
            Response response = new Response();
            response.setStatus("1");
            response.setMessage("Dapil deleted successfully");
            response.setData(null);

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
