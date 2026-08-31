package com.allobank.splitbill.controller;

import com.allobank.splitbill.dto.request.AddExpenseRequest;
import com.allobank.splitbill.dto.request.CreateGroupRequest;
import com.allobank.splitbill.dto.response.GroupResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("End-to-End Test: Create group, add expense, and verify settlement summary")
    void testEndToEndGroupFlow() throws Exception {
        // 1. Create Group
        CreateGroupRequest createRequest = CreateGroupRequest.builder()
                .name("Dinner Group")
                .participants(List.of("Resa", "Budi", "Siti"))
                .build();

        MvcResult createResult = mockMvc.perform(post("/api/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dinner Group"))
                .andExpect(jsonPath("$.participants.length()").value(3))
                .andReturn();

        String createResponseBody = createResult.getResponse().getContentAsString();
        GroupResponse createdGroup = objectMapper.readValue(createResponseBody, GroupResponse.class);
        Long groupId = createdGroup.getId();
        Long resaId = createdGroup.getParticipants().get(0).getId();

        // 2. Add Expense
        AddExpenseRequest expenseRequest = AddExpenseRequest.builder()
                .description("Team Dinner")
                .totalAmount(new BigDecimal("300.00"))
                .paidByParticipantId(resaId)
                .build();

        mockMvc.perform(post("/api/v1/groups/" + groupId + "/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Team Dinner"))
                .andExpect(jsonPath("$.totalAmount").value(300.00));

        // 3. Verify Settlement Summary
        mockMvc.perform(get("/api/v1/groups/" + groupId + "/settlement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(groupId))
                .andExpect(jsonPath("$.totalGroupExpenses").value(300.00))
                .andExpect(jsonPath("$.service_charge_pct").value(5))
                .andExpect(jsonPath("$.service_charge_amount").value(15.00))
                .andExpect(jsonPath("$.settlements").isArray());
    }
}
