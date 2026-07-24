package com.allobankdev.splitbill.service;

import com.allobankdev.splitbill.dto.group.BillGroupRequestDTO;
import com.allobankdev.splitbill.dto.group.BillGroupResponseDTO;
import com.allobankdev.splitbill.entity.BillGroup;
import com.allobankdev.splitbill.mapper.EntityMapper;
import com.allobankdev.splitbill.repository.BillGroupRepository;
import com.allobankdev.splitbill.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillGroupService {

    private final BillGroupRepository billGroupRepository;
    private final EntityMapper entityMapper;

    @Transactional
    public BillGroupResponseDTO createGroup(BillGroupRequestDTO request) {
        BillGroup group = BillGroup.builder()
                .name(request.getName())
                .participants(request.getParticipants())
                .build();
        
        BillGroup savedGroup = billGroupRepository.save(group);
        return entityMapper.toGroupResponseDTO(savedGroup);
    }

    public BillGroup getGroupEntityById(String groupId) {
        return billGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
    }

    public List<BillGroupResponseDTO> getAllGroups() {
        return billGroupRepository.findAll().stream()
                .map(entityMapper::toGroupResponseDTO)
                .collect(Collectors.toList());
    }
}
