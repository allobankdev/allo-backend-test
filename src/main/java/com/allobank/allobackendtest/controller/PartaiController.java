package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.common.Response;
import com.allobank.allobackendtest.entity.Partai;
import com.allobank.allobackendtest.service.PartaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partai")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PartaiController {

    @Autowired
    private PartaiService partaiService;

    @PostMapping("/create")
    public ResponseEntity<Response> createPartai(@RequestBody Partai partai) {
        try {
            Partai savedPartai = partaiService.savePartai(partai);
            Response response = new Response();
            response.setStatus("1");
            response.setMessage("Partai created successfully");
            response.setData(savedPartai);

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
    public ResponseEntity<Response> getPartaiById(@PathVariable Long id) {
        try {
            Partai partai = partaiService.getPartaiById(id);
            Response response = new Response();
            response.setStatus("1");
            response.setMessage("OK");
            response.setData(partai);

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
    public ResponseEntity<Response> getAllPartais() {
        try {
            List<Partai> partais = partaiService.getAllPartais();
            Response response = new Response();
            response.setStatus("1");
            response.setMessage("OK");
            response.setData(partais);

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
    public ResponseEntity<Response> deletePartai(@PathVariable Long id) {
        try {
            partaiService.deletePartai(id);
            Response response = new Response();
            response.setStatus("1");
            response.setMessage("Partai deleted successfully");
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
