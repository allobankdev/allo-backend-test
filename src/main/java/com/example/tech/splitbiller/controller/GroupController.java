package com.example.tech.splitbiller.controller;

import com.example.tech.splitbiller.model.request.CreateGroupRequest;
import com.example.tech.splitbiller.model.request.UpdateGroupRequest;
import com.example.tech.splitbiller.model.response.BaseResponse;
import com.example.tech.splitbiller.model.response.GroupResponse;
import com.example.tech.splitbiller.service.GroupService;
import com.example.tech.splitbiller.util.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.API_PATH + Constants.GROUP_PATH)
@Validated
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping(value = Constants.CREATE_PATH)
    public ResponseEntity<BaseResponse<GroupResponse>> createGroup(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                                                   @Valid @RequestBody CreateGroupRequest request) {

        GroupResponse response = groupService.createGroup(idempotencyKey, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.<GroupResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @GetMapping(value = Constants.GET_PATH + "/{group_id}")
    public ResponseEntity<BaseResponse<GroupResponse>> getGroup(@PathVariable("group_id") String groupId) {
        GroupResponse response = groupService.getGroup(groupId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<GroupResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @PutMapping(value = Constants.UPDATE_PATH + "/{group_id}")
    public ResponseEntity<BaseResponse<GroupResponse>> updateGroup(@PathVariable("group_id") String groupId,
                                                                   @RequestHeader("Idempotency-Key") String idempotencyKey,
                                                                   @Valid @RequestBody UpdateGroupRequest request) {
        GroupResponse response = groupService.updateGroup(groupId, idempotencyKey, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<GroupResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }
}
