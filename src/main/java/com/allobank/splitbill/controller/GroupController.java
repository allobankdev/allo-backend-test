package com.allobank.splitbill.controller;

import com.allobank.splitbill.dto.request.CreateGroupRequest;
import com.allobank.splitbill.dto.response.GroupResponse;
import com.allobank.splitbill.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "Bill Groups", description = "Endpoints for creating and retrieving bill groups")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "Create a new bill group", description = "Creates a new group with a name and a list of participant names")
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        GroupResponse response = groupService.createGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "Get group details", description = "Retrieves group information and its participants by group ID")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable Long groupId) {
        GroupResponse response = groupService.getGroupResponse(groupId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all groups", description = "Retrieves all created bill groups")
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }
}
