package com.allobankdev.split_bill_api.service;

import com.allobankdev.split_bill_api.dto.SettlementResponse;

public interface SettlementService {

    SettlementResponse getSettlement(Long groupId);
}
