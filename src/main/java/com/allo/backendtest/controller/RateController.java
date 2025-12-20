package com.allo.backendtest.controller;

import com.allo.backendtest.dto.ResponseWrapper;
import com.allo.backendtest.exception.HttpException;
import com.allo.backendtest.store.BaseStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class RateController {

    private final Map<String, BaseStore<?>> baseStores;

    public RateController(Map<String, BaseStore<?>> baseStore) {
        this.baseStores = baseStore;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<ResponseWrapper> getRate(@PathVariable("resourceType") String resourceType) throws HttpException {
        var response = new ResponseWrapper();

        BaseStore<?> store = baseStores.get(resourceType.toLowerCase());
        if(store == null) throw new HttpException(404,"Resource type '" + resourceType + "' not found.");

        try{
            response.setData(store.getData());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            throw new HttpException(500,e.getMessage());
        }
    }

}
