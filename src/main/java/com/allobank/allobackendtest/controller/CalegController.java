package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/caleg")
public class CalegController {

    @Autowired
    private CalegService calegService;

    @GetMapping
    public List<Caleg> getAllCaleg() {
        return calegService.getAllCaleg();
    }

    @GetMapping("/dapil/{dapilId}")
    public List<Caleg> getCalegByDapil(@PathVariable UUID dapilId) {
        return calegService.getCalegByDapil(dapilId);
    }

    @GetMapping("/partai/{partaiId}")
    public List<Caleg> getCalegByPartai(@PathVariable UUID partaiId) {
        return calegService.getCalegByPartai(partaiId);
    }

    @GetMapping("/filter")
    public List<Caleg> getCalegByDapilAndPartai(
            @RequestParam UUID dapilId,
            @RequestParam UUID partaiId) {
        return calegService.getCalegByDapilAndPartai(dapilId, partaiId);
    }

    @GetMapping("/sorted")
    public List<Caleg> getCalegSortedByNomorUrut() {
        return calegService.getCalegSortedByNomorUrut();
    }
}
