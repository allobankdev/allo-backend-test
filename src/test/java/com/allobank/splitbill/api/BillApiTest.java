package com.allobank.splitbill.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:splitbilltest;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
class BillApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test
    void createsGroupExpenseAndSettlement() throws Exception {
        JsonNode group = createGroup("Trip", "Alice", "Bob", "Charlie");
        String id = group.get("id").asText();
        String alice = group.get("participants").get(0).get("username").asText();

        String expenseJson = mvc.perform(post("/api/groups/{id}/expenses", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Dinner\",\"amount\":100.00,\"paid_by\":\"" + alice
                                + "\",\"split_type\":\"EQUAL\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        JsonNode expense = mapper.readTree(expenseJson);
        assertEquals("33.34", expense.get("shares").get(0).get("amount").decimalValue().toPlainString());

        String settlementJson = mvc.perform(get("/api/groups/{id}/settlement", id))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JsonNode settlement = mapper.readTree(settlementJson);
        assertEquals(5, settlement.get("service_charge_pct").asInt());
        assertEquals(0, new BigDecimal("5.00").compareTo(
                settlement.get("service_charge_amount").decimalValue()));
        assertEquals(2, settlement.get("transfers").size());
        assertEquals("IDR", settlement.get("currency").asText());
    }

    @Test
    void createsGlobalUserAndRejectsDuplicateUsernameIgnoringCase() throws Exception {
        JsonNode user = createUser("UniqueUser", "Unique User");
        assertEquals("@uniqueuser", user.get("username").asText());

        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"@UNIQUEUSER\",\"display_name\":\"Duplicate\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void recordsPaymentShowsPersonalBillsAndReversesWithoutDeletingLedger() throws Exception {
        JsonNode group = createGroup("Payment trip", "PayAlice", "PayBob");
        String groupId = group.get("id").asText();
        String alice = group.get("participants").get(0).get("id").asText();
        String bob = group.get("participants").get(1).get("id").asText();

        mvc.perform(post("/api/groups/{id}/expenses", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Dinner\",\"amount\":100.00,\"paid_by\":\""
                                + group.get("participants").get(0).get("username").asText()
                                + "\",\"split_type\":\"EQUAL\"}"))
                .andExpect(status().isCreated());

        JsonNode before = mapper.readTree(mvc.perform(get("/api/groups/{id}/settlement", groupId))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertMoney("50.00", before.get("transfers").get(0).get("amount"));

        String paymentRequest = "{\"payer\":\"" + bob + "\",\"receiver\":\"" + alice
                + "\",\"amount\":20.00}";
        JsonNode payment = mapper.readTree(mvc.perform(post("/api/groups/{id}/payments", groupId)
                        .contentType(MediaType.APPLICATION_JSON).content(paymentRequest))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());
        assertEquals("PAYMENT", payment.get("type").asText());

        JsonNode afterPayment = mapper.readTree(mvc.perform(get("/api/groups/{id}/settlement", groupId))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertMoney("30.00", afterPayment.get("transfers").get(0).get("amount"));

        JsonNode bills = mapper.readTree(mvc.perform(get("/api/users/{username}/bills", "@paybob"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertMoney("30.00", bills.get("totals_by_currency").get(0).get("you_owe"));
        assertEquals(groupId, bills.get("groups").get(0).get("group_id").asText());
        assertEquals(bob, bills.get("groups").get(0).get("participant_id").asText());

        String paymentId = payment.get("id").asText();
        JsonNode reversal = mapper.readTree(mvc.perform(post(
                        "/api/groups/{groupId}/payments/{paymentId}/reversal", groupId, paymentId))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());
        assertEquals("REVERSAL", reversal.get("type").asText());
        assertEquals(paymentId, reversal.get("reversal_of").asText());
        assertEquals(alice, reversal.get("payer").asText());

        JsonNode ledger = mapper.readTree(mvc.perform(get("/api/groups/{id}/payments", groupId))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertEquals(2, ledger.size());

        JsonNode afterReversal = mapper.readTree(mvc.perform(get("/api/groups/{id}/settlement", groupId))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertMoney("50.00", afterReversal.get("transfers").get(0).get("amount"));

        mvc.perform(post("/api/groups/{groupId}/payments/{paymentId}/reversal", groupId, paymentId))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsPaymentThatExceedsOutstandingBalance() throws Exception {
        JsonNode group = createGroup("Overpayment", "OverAlice", "OverBob");
        String groupId = group.get("id").asText();
        String alice = group.get("participants").get(0).get("id").asText();
        String bob = group.get("participants").get(1).get("id").asText();
        mvc.perform(post("/api/groups/{id}/expenses", groupId).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Dinner\",\"amount\":100.00,\"paid_by\":\""
                                + group.get("participants").get(0).get("username").asText()
                                + "\",\"split_type\":\"EQUAL\"}"))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/groups/{id}/payments", groupId).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payer\":\"" + bob + "\",\"receiver\":\"" + alice
                                + "\",\"amount\":50.01}"))
                .andExpect(status().isConflict());
    }

    @Test
    void personalBillsAggregateMultipleGroupsWithoutMixingCurrencies() throws Exception {
        createUser("CrossAlice", "Cross Alice");
        createUser("CrossBob", "Cross Bob");
        createUser("CrossCara", "Cross Cara");
        JsonNode idrGroup = createGroupForExistingUsers(
                "IDR group", "IDR", "CrossAlice", "CrossBob");
        JsonNode usdGroup = createGroupForExistingUsers(
                "USD group", "USD", "CrossAlice", "CrossCara");

        createEqualExpense(idrGroup, "100.00");
        createEqualExpense(usdGroup, "40.00");

        JsonNode bills = mapper.readTree(mvc.perform(get("/api/users/{username}/bills", "@crossalice"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertEquals(2, bills.get("groups").size());
        assertEquals(2, bills.get("totals_by_currency").size());
        assertEquals("IDR", bills.get("totals_by_currency").get(0).get("currency").asText());
        assertMoney("50.00", bills.get("totals_by_currency").get(0).get("you_are_owed"));
        assertEquals("USD", bills.get("totals_by_currency").get(1).get("currency").asText());
        assertMoney("20.00", bills.get("totals_by_currency").get(1).get("you_are_owed"));
    }

    @Test
    void createsExclusiveTaxExpenseAndSettlesGrossAmount() throws Exception {
        JsonNode group = createGroup("Tax lunch", "Ayu", "Bima");
        String groupId = group.get("id").asText();
        String ayu = group.get("participants").get(0).get("username").asText();
        String bima = group.get("participants").get(1).get("username").asText();

        String request = """
                {
                  "description": "Lunch before PPN",
                  "amount": 100000.00,
                  "tax": {"mode": "EXCLUSIVE", "rate_pct": 11.00},
                  "paid_by": "%s",
                  "split_type": "EXACT",
                  "shares": [
                    {"username": "%s", "amount": 60000.00},
                    {"username": "%s", "amount": 40000.00}
                  ]
                }
                """.formatted(ayu, ayu, bima);
        JsonNode expense = mapper.readTree(mvc.perform(post("/api/groups/{id}/expenses", groupId)
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());

        assertMoney("100000.00", expense.get("subtotal"));
        assertMoney("11000.00", expense.get("tax_amount"));
        assertMoney("111000.00", expense.get("amount"));
        assertMoney("66600.00", expense.get("shares").get(0).get("amount"));
        assertMoney("44400.00", expense.get("shares").get(1).get("amount"));

        JsonNode settlement = mapper.readTree(mvc.perform(get("/api/groups/{id}/settlement", groupId))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertMoney("11000.00", settlement.get("total_tax"));
        assertMoney("111000.00", settlement.get("total_expenses"));
        assertMoney("44400.00", settlement.get("transfers").get(0).get("amount"));
    }

    @Test
    void createsInclusiveTaxExpenseAndRejectsInvalidTaxConfiguration() throws Exception {
        JsonNode group = createGroup("Inclusive bill", "Dina", "Eko");
        String groupId = group.get("id").asText();
        String dina = group.get("participants").get(0).get("username").asText();

        String validRequest = """
                {
                  "description": "Receipt already includes PPN",
                  "amount": 111000.00,
                  "tax": {"mode": "INCLUSIVE", "rate_pct": 11.00},
                  "paid_by": "%s",
                  "split_type": "EQUAL"
                }
                """.formatted(dina);
        JsonNode expense = mapper.readTree(mvc.perform(post("/api/groups/{id}/expenses", groupId)
                        .contentType(MediaType.APPLICATION_JSON).content(validRequest))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());

        assertMoney("100000.00", expense.get("subtotal"));
        assertMoney("11000.00", expense.get("tax_amount"));
        assertMoney("111000.00", expense.get("amount"));

        String invalidRequest = """
                {
                  "description": "Invalid tax",
                  "amount": 100.00,
                  "tax": {"mode": "NONE", "rate_pct": 11.00},
                  "paid_by": "%s",
                  "split_type": "EQUAL"
                }
                """.formatted(dina);
        mvc.perform(post("/api/groups/{id}/expenses", groupId)
                        .contentType(MediaType.APPLICATION_JSON).content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsPayerOutsideTheGroup() throws Exception {
        JsonNode group = createGroup("Invalid payer", "Fani", "Gilang");
        String request = """
                {
                  "description": "Invalid payer",
                  "amount": 100.00,
                  "paid_by": "@outsider",
                  "split_type": "EQUAL"
                }
                """;

        mvc.perform(post("/api/groups/{id}/expenses", group.get("id").asText())
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsNullShareElementAsBadRequest() throws Exception {
        JsonNode group = createGroup("Invalid share", "Hana", "Irfan");
        String groupId = group.get("id").asText();
        String payer = group.get("participants").get(0).get("username").asText();

        String request = """
                {
                  "description": "Invalid share",
                  "amount": 100.00,
                  "paid_by": "%s",
                  "split_type": "EXACT",
                  "shares": [null]
                }
                """.formatted(payer);

        mvc.perform(post("/api/groups/{id}/expenses", groupId)
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    private JsonNode createGroup(String name, String... participantNames) throws Exception {
        for (String participantName : participantNames) {
            createUser(participantName, participantName);
        }
        return createGroupForExistingUsers(name, "IDR", participantNames);
    }

    private JsonNode createGroupForExistingUsers(String name, String currency,
                                                   String... participantNames) throws Exception {
        var participants = mapper.createArrayNode();
        for (String participantName : participantNames) {
            participants.add("@" + participantName.toLowerCase());
        }
        var request = mapper.createObjectNode();
        request.put("name", name);
        request.put("currency", currency);
        request.set("participants", participants);
        String response = mvc.perform(post("/api/groups").contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return mapper.readTree(response);
    }

    private void createEqualExpense(JsonNode group, String amount) throws Exception {
        String groupId = group.get("id").asText();
        String payer = group.get("participants").get(0).get("username").asText();
        mvc.perform(post("/api/groups/{id}/expenses", groupId).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Shared cost\",\"amount\":" + amount
                                + ",\"paid_by\":\"" + payer + "\",\"split_type\":\"EQUAL\"}"))
                .andExpect(status().isCreated());
    }

    private JsonNode createUser(String username, String displayName) throws Exception {
        var request = mapper.createObjectNode();
        request.put("username", "@" + username);
        request.put("display_name", displayName);
        String response = mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return mapper.readTree(response);
    }

    private void assertMoney(String expected, JsonNode actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual.decimalValue()));
    }
}
