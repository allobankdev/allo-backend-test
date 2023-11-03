package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.CalegResponse;
import com.allobank.allobackendtest.model.WebResponse;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CalegControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CalegRepository calegRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test

	// Without Filter
	void filterNotFound() throws Exception {
		mockMvc.perform(
			get("/api/caleg-list")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpectAll(
			status().isOk()
		).andDo(result -> {
			WebResponse<List<CalegResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
			});
			assertNull(response.getErrors());
			assertEquals(4, response.getData().size());
			assertEquals(1, response.getPaging().getTotalPage());
			assertEquals(0, response.getPaging().getCurrentPage());
			assertEquals(10, response.getPaging().getSize());


		});
	}

	// Filter By Dapil
	@Test
	void filterByDapil() throws Exception {
		mockMvc.perform(
			get("/api/caleg-list")
				.queryParam("dapil_id", "1")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpectAll(
			status().isOk()
		).andDo(result -> {
			WebResponse<List<CalegResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
			});
			assertNull(response.getErrors());
			assertEquals(2, response.getData().size());
			assertEquals(1, response.getPaging().getTotalPage());
			assertEquals(0, response.getPaging().getCurrentPage());
			assertEquals(10, response.getPaging().getSize());


		});
	}

	// Filter By Partai
	@Test
	void filterByPartai() throws Exception {
		mockMvc.perform(
			get("/api/caleg-list")
				.queryParam("partai_id", "1")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpectAll(
			status().isOk()
		).andDo(result -> {
			WebResponse<List<CalegResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
			});
			assertNull(response.getErrors());
			assertEquals(2, response.getData().size());
			assertEquals(1, response.getPaging().getTotalPage());
			assertEquals(0, response.getPaging().getCurrentPage());
			assertEquals(10, response.getPaging().getSize());


		});
	}

	// Filter By Nomor Urut
	@Test
	void filterByNomorUrut() throws Exception {
		mockMvc.perform(
			get("/api/caleg-list")
				.queryParam("nomor_urut", "1")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpectAll(
			status().isOk()
		).andDo(result -> {
			WebResponse<List<CalegResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
			});
			assertNull(response.getErrors());
			assertEquals(2, response.getData().size());
			assertEquals(1, response.getPaging().getTotalPage());
			assertEquals(0, response.getPaging().getCurrentPage());
			assertEquals(10, response.getPaging().getSize());


		});
	}

}
