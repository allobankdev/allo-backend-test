package com.allobankdev.split_bill_api.controller;

import com.allobankdev.split_bill_api.dto.CreateGroupRequest;
import com.allobankdev.split_bill_api.entity.BillGroup;
import com.allobankdev.split_bill_api.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public BillGroup createGroup(
            @Valid
            @RequestBody
            CreateGroupRequest request) {

        return groupService.createGroup(request);
    }

    @GetMapping("/{groupId}")
    public BillGroup getGroup(
            @PathVariable Long groupId) {

        return groupService.getGroup(groupId);
    }
}
