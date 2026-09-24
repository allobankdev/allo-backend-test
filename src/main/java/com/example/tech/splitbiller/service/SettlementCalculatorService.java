package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.model.response.DetailSettlementResponse;
import com.example.tech.splitbiller.model.response.PersonBalanceResponse;
import com.example.tech.splitbiller.model.response.SettlementResponse;
import com.example.tech.splitbiller.util.GreedySettlementOptimizer;
import com.example.tech.splitbiller.util.ServiceChargeCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SettlementCalculatorService {

    private final ExpenseService expenseService;
    private final BalanceCalculatorService balanceCalculatorService;
    private final GreedySettlementOptimizer optimizer;
    private final ServiceChargeCalculator serviceChargeCalculator;

    public SettlementCalculatorService(
            ExpenseService expenseService,
            BalanceCalculatorService balanceCalculatorService,
            GreedySettlementOptimizer optimizer,
            ServiceChargeCalculator serviceChargeCalculator) {
        this.expenseService = expenseService;
        this.balanceCalculatorService = balanceCalculatorService;
        this.optimizer = optimizer;
        this.serviceChargeCalculator = serviceChargeCalculator;
    }

    public DetailSettlementResponse calculate(String groupId) {
        List<PersonBalanceResponse> balances = balanceCalculatorService.calculate(groupId);
        List<SettlementResponse> settlements = optimizer.optimize(balances);
        BigDecimal totalGroupExpenses = expenseService.calculateTotalExpenses(groupId);

        BigDecimal serviceChargePct = serviceChargeCalculator.calculatePercentage();
        BigDecimal serviceChargeAmount = serviceChargeCalculator.calculateAmount(totalGroupExpenses);

        return new DetailSettlementResponse(settlements, serviceChargePct, serviceChargeAmount);
    }
}
