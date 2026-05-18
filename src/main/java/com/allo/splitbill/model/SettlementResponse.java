package com.allo.splitbill.model;

import java.math.BigDecimal;
import java.util.List;

public class SettlementResponse {

    private String groupId;
    private String groupName;
    private BigDecimal totalExpenses;
    private List<Debt> debts;
    private int serviceChargePct;
    private BigDecimal serviceChargeAmount;

    public SettlementResponse() {
    }

    public SettlementResponse(String groupId, String groupName, BigDecimal totalExpenses,
                              List<Debt> debts, int serviceChargePct, BigDecimal serviceChargeAmount) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.totalExpenses = totalExpenses;
        this.debts = debts;
        this.serviceChargePct = serviceChargePct;
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public int getServiceChargePct() {
        return serviceChargePct;
    }

    public void setServiceChargePct(int serviceChargePct) {
        this.serviceChargePct = serviceChargePct;
    }

    public BigDecimal getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(BigDecimal serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }
}
