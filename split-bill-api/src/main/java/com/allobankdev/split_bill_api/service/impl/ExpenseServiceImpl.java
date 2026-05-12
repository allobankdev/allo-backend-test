package com.allobankdev.split_bill_api.service.impl;

import com.allobankdev.split_bill_api.dto.AddExpenseRequest;
import com.allobankdev.split_bill_api.entity.BillGroup;
import com.allobankdev.split_bill_api.entity.Expense;
import com.allobankdev.split_bill_api.entity.ExpenseSplit;
import com.allobankdev.split_bill_api.entity.Participant;
import com.allobankdev.split_bill_api.exception.BadRequestException;
import com.allobankdev.split_bill_api.exception.NotFoundException;
import com.allobankdev.split_bill_api.repository.ExpenseRepository;
import com.allobankdev.split_bill_api.repository.GroupRepository;
import com.allobankdev.split_bill_api.repository.ParticipantRepository;
import com.allobankdev.split_bill_api.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl
        implements ExpenseService {

    private final GroupRepository groupRepository;

    private final ParticipantRepository
            participantRepository;

    private final ExpenseRepository expenseRepository;

    @Override
    public Expense addExpense(
            Long groupId,
            AddExpenseRequest request) {

        BillGroup group =
                groupRepository.findById(groupId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Group not found"
                                ));

        Participant payer =
                participantRepository.findById(
                                request.getPaidBy())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Participant not found"
                                ));

        Expense expense = new Expense();

        expense.setDescription(
                request.getDescription());

        expense.setAmount(
                request.getAmount());

        expense.setGroup(group);

        expense.setPaidBy(payer);

        expense.setSplitStrategy(
                request.getSplitStrategy());

        int participantCount = request.getParticipantIds().size();

        if (participantCount == 0) {
            throw new BadRequestException(
                    "Participants required");
        }

        BigDecimal splitAmount =
                request.getAmount()
                        .divide(
                                BigDecimal.valueOf(
                                        participantCount),
                                2,
                                RoundingMode.HALF_UP
                        );

        for (Long participantId :
                request.getParticipantIds()) {

            Participant participant =
                    participantRepository.findById(
                                    participantId)
                            .orElseThrow(() ->
                                    new NotFoundException(
                                            "Participant not found"
                                    ));

            ExpenseSplit split = new ExpenseSplit();

            split.setExpense(expense);

            split.setParticipant(participant);

            split.setAmount(splitAmount);

            expense.getSplits().add(split);
        }

        return expenseRepository.save(expense);
    }
}
