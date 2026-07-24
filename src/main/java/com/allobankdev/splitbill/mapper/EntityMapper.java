package com.allobankdev.splitbill.mapper;

import com.allobankdev.splitbill.dto.expense.ExpenseResponseDTO;
import com.allobankdev.splitbill.dto.group.BillGroupResponseDTO;
import com.allobankdev.splitbill.entity.BillGroup;
import com.allobankdev.splitbill.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public BillGroupResponseDTO toGroupResponseDTO(BillGroup group) {
        if (group == null) return null;
        return BillGroupResponseDTO.builder()
                .id(group.getId())
                .name(group.getName())
                .participants(group.getParticipants())
                .build();
    }

    public ExpenseResponseDTO toExpenseResponseDTO(Expense expense) {
        if (expense == null) return null;
        return ExpenseResponseDTO.builder()
                .id(expense.getId())
                .groupId(expense.getBillGroup().getId())
                .description(expense.getDescription())
                .paidBy(expense.getPaidBy())
                .amount(expense.getAmount())
                .splitAmong(expense.getSplitAmong())
                .build();
    }
}
