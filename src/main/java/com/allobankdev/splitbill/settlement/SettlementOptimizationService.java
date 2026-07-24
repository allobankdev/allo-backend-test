package com.allobankdev.splitbill.settlement;

import com.allobankdev.splitbill.dto.settlement.TransactionDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class SettlementOptimizationService {

    /**
     * Optimizes settlements to minimize the number of transactions.
     * 
     * @param balances A map of participant name to their net balance.
     *                 Positive balance means they should receive money.
     *                 Negative balance means they owe money.
     * @return A list of transactions to settle all debts.
     */
    public List<TransactionDTO> optimizeSettlements(Map<String, BigDecimal> balances) {
        List<TransactionDTO> transactions = new ArrayList<>();
        
        // Debtors (negative balance) and Creditors (positive balance)
        PriorityQueue<Map.Entry<String, BigDecimal>> debtors = new PriorityQueue<>(
                Comparator.comparing(Map.Entry::getValue) // ascending, most negative first
        );
        
        PriorityQueue<Map.Entry<String, BigDecimal>> creditors = new PriorityQueue<>(
                (a, b) -> b.getValue().compareTo(a.getValue()) // descending, most positive first
        );
        
        for (Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
            // Filter out small precision artifacts (e.g. 0.0000000001)
            if (entry.getValue().abs().compareTo(new BigDecimal("0.01")) >= 0) {
                if (entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
                    debtors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
                } else if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                    creditors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
                }
            }
        }
        
        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<String, BigDecimal> debtor = debtors.poll();
            Map.Entry<String, BigDecimal> creditor = creditors.poll();
            
            BigDecimal debt = debtor.getValue().abs();
            BigDecimal credit = creditor.getValue();
            
            BigDecimal settledAmount = debt.min(credit);
            
            transactions.add(TransactionDTO.builder()
                    .from(debtor.getKey())
                    .to(creditor.getKey())
                    .amount(settledAmount.setScale(2, RoundingMode.HALF_UP))
                    .build());
            
            BigDecimal remainingDebt = debt.subtract(settledAmount);
            BigDecimal remainingCredit = credit.subtract(settledAmount);
            
            if (remainingDebt.compareTo(new BigDecimal("0.01")) >= 0) {
                debtors.add(new AbstractMap.SimpleEntry<>(debtor.getKey(), remainingDebt.negate()));
            }
            if (remainingCredit.compareTo(new BigDecimal("0.01")) >= 0) {
                creditors.add(new AbstractMap.SimpleEntry<>(creditor.getKey(), remainingCredit));
            }
        }
        
        return transactions;
    }
}
