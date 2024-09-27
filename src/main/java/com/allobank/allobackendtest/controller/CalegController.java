package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.response.ResponseData;
import com.allobank.allobackendtest.services.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/caleg/")
public class CalegController {

    @Autowired
    CalegService calegService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<?> addCaleg(@RequestBody CalegDto calegDto){

        try {
            if(calegService.addCalegSvc(calegDto) == true){
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.getStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "listCaleg", method = RequestMethod.GET)
    public ResponseData getListCalegByFilter(@RequestParam String dapilName, @RequestParam String partaiName
    , @RequestParam  int pageNumber, @RequestParam  int pageSize){

        Page page = null;
        Map<String, Object> response = new HashMap<String, Object>();
        ResponseData calegResponse = new ResponseData();

        try{
            page = calegService.getListCalegByDapilNameandPartaiName(dapilName, partaiName, pageNumber, pageSize);
            calegResponse.setCode(HttpStatus.OK.value());
            calegResponse.setMessage("success");

//            response.put("data", page.getContent());
            calegResponse.setData(page.getContent());

        }catch (Exception e){
            System.out.print("getListCalegByFilter() ");
            calegResponse.setCode(HttpStatus.BAD_REQUEST.value());
            calegResponse.setMessage("failed");
            e.printStackTrace();
        }
        return calegResponse;
    }
}
