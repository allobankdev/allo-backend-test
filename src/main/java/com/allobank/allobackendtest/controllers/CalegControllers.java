package com.allobank.allobackendtest.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.responses.ResponseMsg;
import com.allobank.allobackendtest.services.CalegService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1/caleg")
public class CalegControllers {
    @Autowired
    CalegService calegService;

    @GetMapping("/list")
    public Object listCaleg(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDirection,
            @RequestParam(required = false) String partai,
            @RequestParam(required = false) String dapil) {
        ResponseMsg<HashMap<String, Object>> response = new ResponseMsg<>();
        try {
            if (size == null) {
                size = Integer.MAX_VALUE;
            }

            response = calegService.SearchCaleg(page, size, orderBy, orderDirection, partai, dapil);
            List<?> data = (List<?>) response.getData().get("data");

            if (data.isEmpty()) {
                response.setRc("404");
                response.setRm("Data Caleg Tidak Ditemukan");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setRc("500");
            response.setRm("Terjadi Kesalahan pada Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
