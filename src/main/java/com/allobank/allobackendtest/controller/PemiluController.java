package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.service.PemiluService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/pemilu")
public class PemiluController {
    @Autowired
    PemiluService pemiluService;

    @GetMapping("/getListCaleg")
    public ResponseEntity<List<Map<String, Object>>> getListCaleg(@RequestBody Map<String,Object> param) throws Exception {
        return pemiluService.getListCaleg(param);
    }
}
