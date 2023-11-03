package com.allobank.allobackendtest.controllers;

import com.allobank.allobackendtest.model.CalegResponse;
import com.allobank.allobackendtest.model.PagingResponse;
import com.allobank.allobackendtest.model.SearchCalegRequest;
import com.allobank.allobackendtest.model.WebResponse;
import com.allobank.allobackendtest.service.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CalegController {


	@Autowired
	private CalegService calegService;

	@GetMapping(

		path = "/api/caleg-list",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<List<CalegResponse>> filter(@RequestParam(value = "dapil_id", required = false) String dapil_id,
												   @RequestParam(value = "partai_id", required = false) String partai_id,
												   @RequestParam(value = "jenisKelamin", required = false) String jenisKelamin,
												   @RequestParam(value = "nomor_urut", required = false) String nomor_urut,
												   @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
												   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
		SearchCalegRequest request = SearchCalegRequest.builder()
			.page(page)
			.size(size)
			.dapil_id(dapil_id)
			.partai_id(partai_id)
			.nomor_urut((nomor_urut))
			.jenisKelamin(jenisKelamin)
			.build();

		Page<CalegResponse> calegResponses = calegService.filter(request);
		return WebResponse.<List<CalegResponse>>builder()
			.data(calegResponses.getContent())
			.paging(PagingResponse.builder()
				.currentPage(calegResponses.getNumber())
				.totalPage(calegResponses.getTotalPages())
				.size(calegResponses.getSize())
				.build())
			.build();
	}
}
