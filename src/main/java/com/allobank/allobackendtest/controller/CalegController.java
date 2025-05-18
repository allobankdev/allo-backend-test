package com.allobank.allobackendtest.controller;

import java.util.List;
import java.util.UUID;

import com.allobank.allobackendtest.util.RequestBodyCaleg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import com.allobank.allobackendtest.util.RequestGetCaleg;
import com.allobank.allobackendtest.util.Response;

@RestController
@RequestMapping("/api/pemilu")
public class CalegController {
    @Autowired
    private CalegService calegService;

    @PostMapping("/get/caleg")
    public ResponseEntity<Response> getAllCaleg(@RequestBody RequestGetCaleg caleg) {
        List<Caleg> calegs = calegService.getAllCaleg(caleg);

        Response<Object> response = new Response<>();
        response.setMessage("Success get data");
        response.setData(calegs);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/get/caleg/{id}")
    public ResponseEntity<Response> getCalegById(@PathVariable("id") UUID id) {

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid ID", null, HttpStatus.BAD_REQUEST.value()));
        }
        
        Response response = new Response();
        response.setMessage("Success get data");
        response.setData(calegService.getCalegById(id));
        response.setStatus(HttpStatus.OK.value());  


        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping("/post/caleg")
    public ResponseEntity<Response> createCaleg(@RequestBody RequestBodyCaleg caleg) {
        Response<Object> response = new Response<>();
        response.setMessage("Success create data");
        response.setData(calegService.createCaleg(caleg));
        response.setStatus(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PatchMapping("/update/caleg/{id}")
    public ResponseEntity<Response> updateCaleg(@PathVariable("id") UUID id, @RequestBody RequestBodyCaleg caleg) {
        Response<Object> response = new Response<>();
        response.setMessage("Success update data");
        response.setData(calegService.updateCaleg(id, caleg));
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/delete/caleg/{id}")
    public ResponseEntity<Response> deleteCaleg(@PathVariable("id") UUID id) {
        calegService.deleteCaleg(id);

        Response<Object> response = new Response<>();
        response.setMessage("Success delete data");
        response.setData(null);
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
