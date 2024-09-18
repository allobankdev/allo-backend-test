package com.allobank.allobackendtest.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.error.CalegException;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;

@RestController
@RequestMapping("/api")
public class CalegController {
	
	private final Logger log = LoggerFactory.getLogger(CalegController.class);
	
	@Autowired
	private CalegService calegService;
	
	@PostMapping("/getCaleg")
	public List<Caleg> getCaleg(@RequestBody Map requestBody) throws CalegException {
		log.info("Start getCaleg with requestBody : " + requestBody);
		return calegService.getCaleg(requestBody);
	}
	
}
