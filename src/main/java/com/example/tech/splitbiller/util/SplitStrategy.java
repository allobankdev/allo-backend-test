package com.example.tech.splitbiller.util;

import com.example.tech.splitbiller.entity.Expense;
import com.example.tech.splitbiller.entity.ExpenseShare;
import com.example.tech.splitbiller.model.SplitParticipant;

import java.util.List;

public interface SplitStrategy {

    List<ExpenseShare> calculate(Expense expense, List<SplitParticipant> participants);
}
