package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.Response;
import com.allobank.allobackendtest.service.CalegServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/alloBank/caleg")
@Slf4j
public class CalegController {

    @Autowired
    private CalegServiceInterface calegService;

    @GetMapping("list")
    public ResponseEntity<Response> listAll(
            HttpServletRequest httpreq,
            @RequestParam(required = false) String dapil,
            @RequestParam(required = false) String partai,
            @RequestParam(required = false, defaultValue = "nomorUrut") String sortBy) {

        log.info("[START] CalegController - listAllCaleg");
        Response response = calegService.listAllCaleg(dapil, partai, sortBy);
        log.info("[END] CalegController - listAllCaleg \n");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
