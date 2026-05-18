package com.allo.splitbill.service;

import com.allo.splitbill.model.BillGroup;
import com.allo.splitbill.model.Expense;
import com.allo.splitbill.model.SettlementResponse;
import com.allo.splitbill.repository.BillGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillGroupService {

    private final BillGroupRepository repository;
    private final SettlementService settlementService;

    public BillGroupService(BillGroupRepository repository, SettlementService settlementService) {
        this.repository = repository;
        this.settlementService = settlementService;
    }

    public BillGroup createGroup(String name, List<String> participants) {
        BillGroup group = new BillGroup(name, List.copyOf(participants));
        return repository.save(group);
    }

    public BillGroup addExpense(String groupId, String paidBy, java.math.BigDecimal amount,
                                String description, List<String> splitAmong) {
        BillGroup group = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found: " + groupId));

        if (!group.getParticipants().contains(paidBy)) {
            throw new IllegalArgumentException("Payer is not a group participant: " + paidBy);
        }
        for (String p : splitAmong) {
            if (!group.getParticipants().contains(p)) {
                throw new IllegalArgumentException("Participant not in group: " + p);
            }
        }

        Expense expense = new Expense(paidBy, amount, description, splitAmong);
        group.getExpenses().add(expense);
        return group;
    }

    public BillGroup getGroup(String groupId) {
        return repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found: " + groupId));
    }

    public SettlementResponse getSettlement(String groupId) {
        BillGroup group = getGroup(groupId);
        return settlementService.calculate(group);
    }
}
