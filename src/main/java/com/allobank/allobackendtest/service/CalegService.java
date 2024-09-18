package com.allobank.allobackendtest.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.dao.CalegDAO;
import com.allobank.allobackendtest.dao.DapilDAO;
import com.allobank.allobackendtest.dao.PartaiDAO;
import com.allobank.allobackendtest.error.CalegException;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;

@Service
public class CalegService {
	@Autowired
	private CalegDAO calegDao;
	
	@Autowired
	private PartaiDAO partaiDao;
	
	@Autowired
	private DapilDAO dapilDao;
	
	private final Logger log = LoggerFactory.getLogger(CalegService.class);
	
	List<Caleg> responseList = new LinkedList<>();
	
	//LOGIC BUSINESS
	public List<Caleg> getCaleg(Map requestBody) throws CalegException {
		//AMBIL LIST CALEG DALAM DATABASE DENGAN ASUMSI CALEG TIDAK BANYAK
		log.info("RequestBody : " + requestBody);
		try {
			//CHECK APAKAH ADA FILTER YANG DIKIRIM
			if(requestBody!=null && requestBody.get("switch")!=null) {
				if(null!=requestBody.get("switch") && "1".equals((String) requestBody.get("switch"))) {
					log.info("Start getList by Partai filter");
					Partai partai = partaiDao.findByNamaPartai((String)requestBody.get("namaPartai"));
					
					responseList = calegDao.findByPartai(partai,Sort.by(Sort.Direction.ASC,"nomorUrut"));
				}else if(null!=requestBody.get("switch") && "2".equals((String) requestBody.get("switch"))){
					log.info("Start getList by Dapil filter");
					Dapil dapil = dapilDao.findByNamaDapil((String)requestBody.get("namaDapil"));
					
					responseList = calegDao.findByDapil(dapil,Sort.by(Sort.Direction.ASC,"nomorUrut"));
				}
			}else {
				//TIDAK ADA FILTER YANG DIKIRIM
				responseList = calegDao.findAll(Sort.by(Sort.Direction.ASC,"nomorUrut"));
			}
			log.info("responseList : " + responseList);
		}catch (Exception e) {
			log.info("Error : " + e.getMessage());
			throw new CalegException(e.getMessage());
		}
		
		return responseList;
	}
}
