package com.allo.splitbill.controller;

import com.allo.splitbill.dto.AddExpenseRequest;
import com.allo.splitbill.dto.CreateGroupRequest;
import com.allo.splitbill.model.BillGroup;
import com.allo.splitbill.model.Expense;
import com.allo.splitbill.model.SettlementResponse;
import com.allo.splitbill.service.BillGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class BillGroupController {

    private final BillGroupService billGroupService;

    public BillGroupController(BillGroupService billGroupService) {
        this.billGroupService = billGroupService;
    }

    @PostMapping
    public ResponseEntity<BillGroup> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        BillGroup group = billGroupService.createGroup(request.getName(), request.getParticipants());
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    @PostMapping("/{groupId}/expenses")
    public ResponseEntity<Expense> addExpense(
            @PathVariable String groupId,
            @Valid @RequestBody AddExpenseRequest request) {
        BillGroup group = billGroupService.addExpense(
                groupId, request.getPaidBy(), request.getAmount(),
                request.getDescription(), request.getSplitAmong());
        Expense last = group.getExpenses().get(group.getExpenses().size() - 1);
        return ResponseEntity.status(HttpStatus.CREATED).body(last);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<BillGroup> getGroup(@PathVariable String groupId) {
        BillGroup group = billGroupService.getGroup(groupId);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/{groupId}/settlement")
    public ResponseEntity<SettlementResponse> getSettlement(@PathVariable String groupId) {
        SettlementResponse settlement = billGroupService.getSettlement(groupId);
        return ResponseEntity.ok(settlement);
    }
}
