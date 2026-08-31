package com.allobank.splitbill.service;

import com.allobank.splitbill.domain.entity.BillGroup;
import com.allobank.splitbill.domain.entity.Participant;
import com.allobank.splitbill.dto.request.CreateGroupRequest;
import com.allobank.splitbill.dto.response.GroupResponse;
import com.allobank.splitbill.dto.response.ParticipantResponse;
import com.allobank.splitbill.exception.ResourceNotFoundException;
import com.allobank.splitbill.repository.BillGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final BillGroupRepository billGroupRepository;

    @Transactional
    public GroupResponse createGroup(CreateGroupRequest request) {
        BillGroup group = BillGroup.builder()
                .name(request.getName().trim())
                .build();

        for (String participantName : request.getParticipants()) {
            if (participantName != null && !participantName.isBlank()) {
                Participant participant = Participant.builder()
                        .name(participantName.trim())
                        .build();
                group.addParticipant(participant);
            }
        }

        BillGroup savedGroup = billGroupRepository.save(group);
        return mapToGroupResponse(savedGroup);
    }

    @Transactional(readOnly = true)
    public GroupResponse getGroupResponse(Long groupId) {
        BillGroup group = getGroupEntity(groupId);
        return mapToGroupResponse(group);
    }

    @Transactional(readOnly = true)
    public BillGroup getGroupEntity(Long groupId) {
        return billGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill group not found with id: " + groupId));
    }

    @Transactional(readOnly = true)
    public List<GroupResponse> getAllGroups() {
        return billGroupRepository.findAll().stream()
                .map(this::mapToGroupResponse)
                .collect(Collectors.toList());
    }

    public GroupResponse mapToGroupResponse(BillGroup group) {
        List<ParticipantResponse> participantResponses = group.getParticipants().stream()
                .map(p -> ParticipantResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .build())
                .collect(Collectors.toList());

        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .createdAt(group.getCreatedAt())
                .participants(participantResponses)
                .build();
    }
}
