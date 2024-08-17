package com.allobank.allobackendtest.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.repository.CalegRepo;

@Service
public class PemiluService {
    
    @Autowired
    private CalegRepo calegRepo;

    public Map<String, Object> getListCaleg(Map<String, Object> param) throws Exception {
		Map<String, Object> getAll = new HashMap<String, Object>();

		String dapil = param.get("namaDapil").toString();
        String partai = param.get("namaPartai").toString();

		try {
			String compareData = calegRepo.getListCaleg(dapil, partai);
			getAll.put("data", compareData);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception (e.getMessage());
		}
		return getAll;
	}
}
