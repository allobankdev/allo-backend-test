package com.allobank.allobackendtest.controller;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.service.CalegService;
import com.allobank.allobackendtest.utils.constant.ApiPathConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPathConstant.CALEG_PATH)
public class CalegController {
    @Autowired
    private CalegService calegService;

    @GetMapping("/listall")
    public List<Caleg> getAllCaleg() {
        return calegService.getAllCaleg();
    }

    // API untuk menampilkan Caleg dengan filter dan sorting
    @GetMapping("/list")
    public List<Caleg> getCalegList(
            @RequestParam(required = false) String dapilId,
            @RequestParam(required = false) String partaiId,
            @RequestParam(defaultValue = "nomorUrut") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder
    ) {
        return calegService.getCalegList(dapilId, partaiId, sortBy, sortOrder);
    }
    @GetMapping("/{id}")
    public Caleg getCalegById(@PathVariable String id) {
        return calegService.getCalegById(id);
    }

    @PostMapping
    public Caleg createCaleg(@RequestBody Caleg caleg) {
        return calegService.createCaleg(caleg);
    }

    @PutMapping("/{id}")
    public Caleg updateCaleg(@PathVariable String id, @RequestBody Caleg caleg) {
        return calegService.updateCaleg(id, caleg);
    }

    @DeleteMapping("/{id}")
    public void deleteCaleg(@PathVariable String id) {
        calegService.deleteCaleg(id);
    }
}
