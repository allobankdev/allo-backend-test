package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.entity.Group;
import com.example.tech.splitbiller.entity.GroupMember;
import com.example.tech.splitbiller.entity.IdempotencyKey;
import com.example.tech.splitbiller.entity.Person;
import com.example.tech.splitbiller.exception.DataNotFoundException;
import com.example.tech.splitbiller.exception.TransactionConflictException;
import com.example.tech.splitbiller.mapper.GroupMapper;
import com.example.tech.splitbiller.model.request.CreateGroupRequest;
import com.example.tech.splitbiller.model.request.UpdateGroupRequest;
import com.example.tech.splitbiller.model.response.GroupResponse;
import com.example.tech.splitbiller.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final PersonService personService;
    private final IdempotencyKeyService idempotencyKeyService;
    private final ObjectMapper objectMapper;
    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository,
                        PersonService personService,
                        IdempotencyKeyService idempotencyKeyService,
                        ObjectMapper objectMapper,
                        GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.personService = personService;
        this.idempotencyKeyService = idempotencyKeyService;
        this.objectMapper = objectMapper;
        this.groupMapper = groupMapper;
    }

    public GroupResponse createGroup(String idempotencyKey, CreateGroupRequest request) {
        String hashedRequest = objectMapper.writeValueAsString(request);
        Optional<IdempotencyKey> find = idempotencyKeyService.findByIdempotencyKey(idempotencyKey);
        if(find.isPresent()) {
            IdempotencyKey idempotencyKeyObj = find.get();
            if(!idempotencyKeyObj.getHashRequest().equals(hashedRequest))
                throw new TransactionConflictException("Idempotency-Key was already used with a different request");

            String response = idempotencyKeyObj.getResponse();
            return objectMapper.readValue(response, GroupResponse.class);
        }

        List<Person> people = personService.findAllById(request.personIds());
        if(people.size() != request.personIds().size())
            throw new DataNotFoundException("One or more persons do not exist");


        long epochNow = Instant.now().toEpochMilli();
        Group group = new Group();
        group.setName(request.name());
        group.setDescription(request.description());
        group.setCreatedAt(epochNow);

        for (Person person : people) {
            GroupMember membership = new GroupMember();
            membership.setGroup(group);
            membership.setPerson(person);
            membership.setAddedAt(epochNow);
            group.getMemberships().add(membership);
        }

        Group saved = groupRepository.save(group);
        GroupResponse response = groupMapper.toGroupResponse(saved);
        idempotencyKeyService.save(idempotencyKey, hashedRequest, objectMapper.writeValueAsString(response));
        return response;
    }

    public GroupResponse getGroup(String groupId) {
        Group group = groupRepository.findByIdAndDeleted(groupId, Boolean.FALSE)
                .orElseThrow(() -> new DataNotFoundException("Group not found"));
        return groupMapper.toGroupResponse(group);
    }

    public Group getActiveById(String id) {
        return groupRepository.findByIdAndDeleted(id, Boolean.FALSE)
                .orElseThrow(() -> new DataNotFoundException("Group not found"));
    }

    @Transactional
    public GroupResponse updateGroup(String groupId, String idempotencyKey, UpdateGroupRequest request) {
        String hashedRequest = objectMapper.writeValueAsString(request);
        Optional<IdempotencyKey> find = idempotencyKeyService.findByIdempotencyKey(idempotencyKey);
        if(find.isPresent()) {
            IdempotencyKey idempotencyKeyObj = find.get();
            if(!idempotencyKeyObj.getHashRequest().equals(hashedRequest))
                throw new TransactionConflictException("Idempotency-Key was already used with a different request");

            String response = idempotencyKeyObj.getResponse();
            return objectMapper.readValue(response, GroupResponse.class);
        }

        Group group = groupRepository.findByIdAndDeleted(groupId, Boolean.FALSE)
                .orElseThrow(() -> new DataNotFoundException("Group not found"));

        if(!CollectionUtils.isEmpty(request.personIds())) {
            List<Person> people = personService.findAllById(request.personIds());
            if(people.size() != request.personIds().size())
                throw new DataNotFoundException("One or more persons do not exist");

            group.getMemberships().clear();
            long epochNow = Instant.now().toEpochMilli();
            for (Person person : people) {
                GroupMember membership = new GroupMember();
                membership.setGroup(group);
                membership.setPerson(person);
                membership.setAddedAt(epochNow);
                group.getMemberships().add(membership);
            }
        }
        group.setName(request.name());
        group.setDescription(request.description());

        Group saved = groupRepository.save(group);
        GroupResponse response = groupMapper.toGroupResponse(saved);
        idempotencyKeyService.save(idempotencyKey, hashedRequest, objectMapper.writeValueAsString(response));
        return response;
    }
}
