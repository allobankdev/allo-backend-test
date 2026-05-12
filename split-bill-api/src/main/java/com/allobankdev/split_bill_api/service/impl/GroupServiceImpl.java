package com.allobankdev.split_bill_api.service.impl;

import com.allobankdev.split_bill_api.dto.CreateGroupRequest;
import com.allobankdev.split_bill_api.entity.BillGroup;
import com.allobankdev.split_bill_api.exception.NotFoundException;
import com.allobankdev.split_bill_api.mapper.GroupMapper;
import com.allobankdev.split_bill_api.repository.GroupRepository;
import com.allobankdev.split_bill_api.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    @Override
    public BillGroup createGroup(
            CreateGroupRequest request) {

        BillGroup group =
                groupMapper.toEntity(request);

        return groupRepository.save(group);
    }

    @Override
    public BillGroup getGroup(Long groupId) {

        return groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Group not found"
                        ));
    }
}
