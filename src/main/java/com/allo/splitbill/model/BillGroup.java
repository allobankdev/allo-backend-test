package com.allo.splitbill.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BillGroup {

    private String id;
    private String name;
    private List<String> participants;
    private List<Expense> expenses;

    public BillGroup() {
        this.id = UUID.randomUUID().toString();
        this.expenses = new ArrayList<>();
    }

    public BillGroup(String name, List<String> participants) {
        this();
        this.name = name;
        this.participants = participants;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getTotalExpenses() {
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
