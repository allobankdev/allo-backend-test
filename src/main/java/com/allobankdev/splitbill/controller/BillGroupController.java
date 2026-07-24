package com.allobankdev.splitbill.controller;

import com.allobankdev.splitbill.dto.expense.ExpenseRequestDTO;
import com.allobankdev.splitbill.dto.expense.ExpenseResponseDTO;
import com.allobankdev.splitbill.dto.group.BillGroupRequestDTO;
import com.allobankdev.splitbill.dto.group.BillGroupResponseDTO;
import com.allobankdev.splitbill.dto.settlement.SettlementResponseDTO;
import com.allobankdev.splitbill.service.BillGroupService;
import com.allobankdev.splitbill.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class BillGroupController {

    private final BillGroupService billGroupService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<BillGroupResponseDTO> createGroup(@Valid @RequestBody BillGroupRequestDTO request) {
        BillGroupResponseDTO response = billGroupService.createGroup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BillGroupResponseDTO>> getAllGroups() {
        List<BillGroupResponseDTO> response = billGroupService.getAllGroups();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/expenses")
    public ResponseEntity<ExpenseResponseDTO> addExpense(
            @PathVariable("id") String groupId,
            @Valid @RequestBody ExpenseRequestDTO request) {
        ExpenseResponseDTO response = expenseService.addExpense(groupId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/settlement")
    public ResponseEntity<SettlementResponseDTO> getSettlementSummary(@PathVariable("id") String groupId) {
        SettlementResponseDTO response = expenseService.getSettlementSummary(groupId);
        return ResponseEntity.ok(response);
    }
}
