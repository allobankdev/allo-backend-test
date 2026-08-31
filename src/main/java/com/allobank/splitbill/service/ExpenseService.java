package com.allobank.splitbill.service;

import com.allobank.splitbill.domain.entity.BillGroup;
import com.allobank.splitbill.domain.entity.Expense;
import com.allobank.splitbill.domain.entity.ExpenseSplit;
import com.allobank.splitbill.domain.entity.Participant;
import com.allobank.splitbill.domain.enums.SplitType;
import com.allobank.splitbill.dto.request.AddExpenseRequest;
import com.allobank.splitbill.dto.request.ExpenseSplitRequest;
import com.allobank.splitbill.dto.response.ExpenseResponse;
import com.allobank.splitbill.dto.response.ExpenseSplitResponse;
import com.allobank.splitbill.dto.response.ParticipantResponse;
import com.allobank.splitbill.exception.InvalidExpenseException;
import com.allobank.splitbill.exception.ResourceNotFoundException;
import com.allobank.splitbill.repository.ExpenseRepository;
import com.allobank.splitbill.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ParticipantRepository participantRepository;
    private final GroupService groupService;

    @Transactional
    public ExpenseResponse addExpense(Long groupId, AddExpenseRequest request) {
        BillGroup group = groupService.getGroupEntity(groupId);
        Participant paidBy = participantRepository.findByIdAndGroupId(request.getPaidByParticipantId(), groupId)
                .orElseThrow(() -> new InvalidExpenseException("Payer with id " + request.getPaidByParticipantId() + " does not belong to group " + groupId));

        BigDecimal totalAmount = request.getTotalAmount().setScale(2, RoundingMode.HALF_UP);
        SplitType splitType = request.getSplitType() != null ? request.getSplitType() : SplitType.EQUAL;

        Expense expense = Expense.builder()
                .group(group)
                .description(request.getDescription().trim())
                .totalAmount(totalAmount)
                .paidBy(paidBy)
                .splitType(splitType)
                .build();

        List<ExpenseSplit> splits = calculateSplits(group, expense, request.getSplits(), splitType, totalAmount);
        splits.forEach(expense::addSplit);

        Expense savedExpense = expenseRepository.save(expense);
        return mapToExpenseResponse(savedExpense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByGroup(Long groupId) {
        groupService.getGroupEntity(groupId); // Validate group exists
        return expenseRepository.findByGroupId(groupId).stream()
                .map(this::mapToExpenseResponse)
                .collect(Collectors.toList());
    }

    private List<ExpenseSplit> calculateSplits(BillGroup group, Expense expense, List<ExpenseSplitRequest> splitRequests, SplitType splitType, BigDecimal totalAmount) {
        List<Participant> allGroupParticipants = group.getParticipants();
        if (allGroupParticipants.isEmpty()) {
            throw new InvalidExpenseException("Group has no participants");
        }

        switch (splitType) {
            case EQUAL:
                return calculateEqualSplits(group, splitRequests, totalAmount);
            case EXACT:
                return calculateExactSplits(group, splitRequests, totalAmount);
            case PERCENTAGE:
                return calculatePercentageSplits(group, splitRequests, totalAmount);
            default:
                throw new InvalidExpenseException("Unsupported split type: " + splitType);
        }
    }

    private List<ExpenseSplit> calculateEqualSplits(BillGroup group, List<ExpenseSplitRequest> splitRequests, BigDecimal totalAmount) {
        List<Participant> targetParticipants;
        if (splitRequests == null || splitRequests.isEmpty()) {
            targetParticipants = group.getParticipants();
        } else {
            targetParticipants = new ArrayList<>();
            for (ExpenseSplitRequest req : splitRequests) {
                Participant p = participantRepository.findByIdAndGroupId(req.getParticipantId(), group.getId())
                        .orElseThrow(() -> new InvalidExpenseException("Participant ID " + req.getParticipantId() + " not found in group"));
                targetParticipants.add(p);
            }
        }

        int count = targetParticipants.size();
        if (count == 0) {
            throw new InvalidExpenseException("Equal split requires at least one participant");
        }

        BigDecimal baseShare = totalAmount.divide(BigDecimal.valueOf(count), 2, RoundingMode.DOWN);
        BigDecimal totalAllocated = baseShare.multiply(BigDecimal.valueOf(count));
        BigDecimal remainder = totalAmount.subtract(totalAllocated);

        List<ExpenseSplit> splits = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Participant p = targetParticipants.get(i);
            BigDecimal share = baseShare;
            if (i == 0) {
                share = share.add(remainder); // Add remainder cents to first participant
            }
            splits.add(ExpenseSplit.builder()
                    .participant(p)
                    .shareAmount(share)
                    .build());
        }
        return splits;
    }

    private List<ExpenseSplit> calculateExactSplits(BillGroup group, List<ExpenseSplitRequest> splitRequests, BigDecimal totalAmount) {
        if (splitRequests == null || splitRequests.isEmpty()) {
            throw new InvalidExpenseException("EXACT split requires split details for participants");
        }

        BigDecimal sumExact = BigDecimal.ZERO;
        List<ExpenseSplit> splits = new ArrayList<>();

        for (ExpenseSplitRequest req : splitRequests) {
            if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidExpenseException("Each participant must have a non-negative amount for EXACT split");
            }
            Participant p = participantRepository.findByIdAndGroupId(req.getParticipantId(), group.getId())
                    .orElseThrow(() -> new InvalidExpenseException("Participant ID " + req.getParticipantId() + " not found in group"));

            BigDecimal amount = req.getAmount().setScale(2, RoundingMode.HALF_UP);
            sumExact = sumExact.add(amount);

            splits.add(ExpenseSplit.builder()
                    .participant(p)
                    .shareAmount(amount)
                    .build());
        }

        if (sumExact.compareTo(totalAmount) != 0) {
            throw new InvalidExpenseException("Sum of exact splits (" + sumExact + ") does not equal expense total amount (" + totalAmount + ")");
        }

        return splits;
    }

    private List<ExpenseSplit> calculatePercentageSplits(BillGroup group, List<ExpenseSplitRequest> splitRequests, BigDecimal totalAmount) {
        if (splitRequests == null || splitRequests.isEmpty()) {
            throw new InvalidExpenseException("PERCENTAGE split requires split details for participants");
        }

        BigDecimal totalPercentage = BigDecimal.ZERO;
        List<ExpenseSplit> splits = new ArrayList<>();
        BigDecimal sumCalculatedAmount = BigDecimal.ZERO;

        for (ExpenseSplitRequest req : splitRequests) {
            if (req.getPercentage() == null || req.getPercentage().compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidExpenseException("Each participant must have a non-negative percentage for PERCENTAGE split");
            }
            Participant p = participantRepository.findByIdAndGroupId(req.getParticipantId(), group.getId())
                    .orElseThrow(() -> new InvalidExpenseException("Participant ID " + req.getParticipantId() + " not found in group"));

            BigDecimal percentage = req.getPercentage().setScale(2, RoundingMode.HALF_UP);
            totalPercentage = totalPercentage.add(percentage);

            BigDecimal calculatedShare = totalAmount.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            sumCalculatedAmount = sumCalculatedAmount.add(calculatedShare);

            splits.add(ExpenseSplit.builder()
                    .participant(p)
                    .shareAmount(calculatedShare)
                    .sharePercentage(percentage)
                    .build());
        }

        if (totalPercentage.compareTo(new BigDecimal("100.00")) != 0 && totalPercentage.compareTo(new BigDecimal("100")) != 0) {
            throw new InvalidExpenseException("Sum of split percentages (" + totalPercentage + "%) must equal 100%");
        }

        // Adjust rounding drift in share amounts if needed
        BigDecimal drift = totalAmount.subtract(sumCalculatedAmount);
        if (drift.compareTo(BigDecimal.ZERO) != 0 && !splits.isEmpty()) {
            ExpenseSplit first = splits.get(0);
            first.setShareAmount(first.getShareAmount().add(drift));
        }

        return splits;
    }

    public ExpenseResponse mapToExpenseResponse(Expense expense) {
        ParticipantResponse paidBy = ParticipantResponse.builder()
                .id(expense.getPaidBy().getId())
                .name(expense.getPaidBy().getName())
                .build();

        List<ExpenseSplitResponse> splitResponses = expense.getSplits().stream()
                .map(s -> ExpenseSplitResponse.builder()
                        .id(s.getId())
                        .participant(ParticipantResponse.builder()
                                .id(s.getParticipant().getId())
                                .name(s.getParticipant().getName())
                                .build())
                        .shareAmount(s.getShareAmount())
                        .sharePercentage(s.getSharePercentage())
                        .build())
                .collect(Collectors.toList());

        return ExpenseResponse.builder()
                .id(expense.getId())
                .description(expense.getDescription())
                .totalAmount(expense.getTotalAmount())
                .splitType(expense.getSplitType())
                .paidBy(paidBy)
                .createdAt(expense.getCreatedAt())
                .splits(splitResponses)
                .build();
    }
}
