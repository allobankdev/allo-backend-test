package com.allobankdev.split_bill_api.service;

import com.allobankdev.split_bill_api.dto.CreateGroupRequest;
import com.allobankdev.split_bill_api.entity.BillGroup;

public interface GroupService {

    BillGroup createGroup(CreateGroupRequest request);

    BillGroup getGroup(Long groupId);
}
