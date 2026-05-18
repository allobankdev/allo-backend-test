package com.allo.splitbill.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Expense {

    private String id;
    private String paidBy;
    private BigDecimal amount;
    private String description;
    private List<String> splitAmong;
    private LocalDateTime createdAt;

    public Expense() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    public Expense(String paidBy, BigDecimal amount, String description, List<String> splitAmong) {
        this();
        this.paidBy = paidBy;
        this.amount = amount;
        this.description = description;
        this.splitAmong = splitAmong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSplitAmong() {
        return splitAmong;
    }

    public void setSplitAmong(List<String> splitAmong) {
        this.splitAmong = splitAmong;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
