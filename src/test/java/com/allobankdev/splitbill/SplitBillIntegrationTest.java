package com.allobankdev.splitbill;

import com.allobankdev.splitbill.dto.expense.ExpenseRequestDTO;
import com.allobankdev.splitbill.dto.group.BillGroupRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SplitBillIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFullFlow() throws Exception {
        // 1. Create Group
        BillGroupRequestDTO groupRequest = new BillGroupRequestDTO();
        groupRequest.setName("Liburan Bali");
        groupRequest.setParticipants(Arrays.asList("Andi", "Budi", "Citra"));

        String groupResponse = mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Liburan Bali")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn().getResponse().getContentAsString();

        String groupId = objectMapper.readTree(groupResponse).get("id").asText();

        // 2. Add Expense 1: Andi paid 150000 for all
        ExpenseRequestDTO exp1 = new ExpenseRequestDTO();
        exp1.setDescription("Makan Siang");
        exp1.setPaidBy("Andi");
        exp1.setAmount(new BigDecimal("150000.00"));

        mockMvc.perform(post("/api/groups/" + groupId + "/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exp1)))
                .andExpect(status().isCreated());

        // 3. Add Expense 2: Budi paid 60000 for Andi and Budi
        ExpenseRequestDTO exp2 = new ExpenseRequestDTO();
        exp2.setDescription("Taksi");
        exp2.setPaidBy("Budi");
        exp2.setAmount(new BigDecimal("60000.00"));
        exp2.setSplitAmong(Arrays.asList("Andi", "Budi"));

        mockMvc.perform(post("/api/groups/" + groupId + "/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exp2)))
                .andExpect(status().isCreated());

        // 4. Get Settlement
        mockMvc.perform(get("/api/groups/" + groupId + "/settlement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalExpenses", is(210000.0)))
                .andExpect(jsonPath("$.serviceChargePct", is(0)))
                .andExpect(jsonPath("$.serviceChargeAmount", is(0.0)))
                .andExpect(jsonPath("$.transactions", hasSize(2)));
    }
}
