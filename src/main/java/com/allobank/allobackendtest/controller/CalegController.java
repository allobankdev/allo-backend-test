package com.allobank.allobackendtest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;

@RestController
@RequestMapping(ApiPath.Caleg_Path)
public class CalegController {
    @Autowired
    private CalegService calegService;

    @GetMapping("/linstall")
    public List<Caleg>getallCalegs(){
        return calegService.getallCalegs();
    }

    @GetMapping("/list")
    public List<Caleg> getCalegList(){
        return calegService.getallCalegs();
    }
}
