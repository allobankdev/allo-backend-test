package com.allobank.splitbill.api;

import com.allobank.splitbill.api.dto.request.CreateExpenseRequest;
import com.allobank.splitbill.api.dto.request.CreateGroupRequest;
import com.allobank.splitbill.api.dto.response.ExpenseResponse;
import com.allobank.splitbill.api.dto.response.GroupResponse;
import com.allobank.splitbill.api.dto.response.SettlementResponse;
import com.allobank.splitbill.service.BillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class BillController {
    private final BillService service;

    public BillController(BillService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        GroupResponse response = service.createGroup(request);
        return ResponseEntity.created(URI.create("/api/groups/" + response.id())).body(response);
    }

    @GetMapping("/{groupId}")
    public GroupResponse getGroup(@PathVariable UUID groupId) {
        return service.getGroup(groupId);
    }

    @PostMapping("/{groupId}/expenses")
    public ResponseEntity<ExpenseResponse> createExpense(@PathVariable UUID groupId,
                                                          @Valid @RequestBody CreateExpenseRequest request) {
        ExpenseResponse response = service.createExpense(groupId, request);
        return ResponseEntity.created(URI.create("/api/groups/" + groupId + "/expenses/" + response.id()))
                .body(response);
    }

    @GetMapping("/{groupId}/expenses")
    public List<ExpenseResponse> getExpenses(@PathVariable UUID groupId) {
        return service.getExpenses(groupId);
    }

    @GetMapping("/{groupId}/expenses/{expenseId}")
    public ExpenseResponse getExpense(@PathVariable UUID groupId, @PathVariable UUID expenseId) {
        return service.getExpense(groupId, expenseId);
    }

    @GetMapping("/{groupId}/settlement")
    public SettlementResponse getSettlement(@PathVariable UUID groupId) {
        return service.getSettlement(groupId);
    }
}
