package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.entity.Group;
import com.example.tech.splitbiller.model.response.ExpenseResponse;
import com.example.tech.splitbiller.model.response.ExpenseShareResponse;
import com.example.tech.splitbiller.model.response.PaymentResponse;
import com.example.tech.splitbiller.model.response.PersonBalanceResponse;
import com.example.tech.splitbiller.model.response.PersonResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class BalanceCalculatorService {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final ExpenseService expenseService;
    private final PaymentService paymentService;


    public BalanceCalculatorService(GroupService groupService,
                                    GroupMemberService groupMemberService,
                                    ExpenseService expenseService,
                                    PaymentService paymentService) {
        this.groupService = groupService;
        this.groupMemberService = groupMemberService;
        this.expenseService = expenseService;
        this.paymentService = paymentService;
    }

    public List<PersonBalanceResponse> calculate(String groupId) {
        Group group = groupService.getActiveById(groupId);

        Map<String, PersonResponse> people = new LinkedHashMap<>();
        Map<String, BigDecimal> balances = new LinkedHashMap<>();

        // 1. Initialize every group member with zero balance
        List<PersonResponse> memberships = groupMemberService.getAllPersonWithInGroup(group);
        for (PersonResponse person : memberships) {
            people.put(person.id(), person);
            balances.put(person.id(), BigDecimal.ZERO);
        }

        // 2. Apply expenses
        List<ExpenseResponse> expenses = expenseService.getAllForBalanceCalculation(group);
        for (ExpenseResponse expense : expenses) {
            String payerId = expense.paidBy();

            // Payer paid the full expense.
            balances.merge(payerId, expense.amount(), BigDecimal::add);

            // Participants owe their share.
            for (ExpenseShareResponse share : expense.shares()) {
                String personId = share.personId();

                balances.merge(personId, share.amount().negate(), BigDecimal::add);
            }
        }

        // 3. Apply payments
        List<PaymentResponse> payments = paymentService.getGroupPayment(groupId);
        for (PaymentResponse payment : payments) {

            String fromPersonId = payment.fromPersonId();
            String toPersonId = payment.toPersonId();
            BigDecimal amount = payment.amount();

            // Sender paid money, so their debt decreases.
            balances.merge(fromPersonId, amount, BigDecimal::add);

            // Receiver received money, so their credit decreases.
            balances.merge(toPersonId, amount.negate(), BigDecimal::add);
        }

        return balances.entrySet()
                .stream()
                .map(entry -> {
                    PersonResponse person = people.get(entry.getKey());
                    return new PersonBalanceResponse(person.id(), person.name(), entry.getValue());
                }).toList();
    }
}
