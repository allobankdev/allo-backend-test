package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.entity.Group;
import com.example.tech.splitbiller.entity.GroupMember;
import com.example.tech.splitbiller.entity.Person;
import com.example.tech.splitbiller.exception.DataInvalidException;
import com.example.tech.splitbiller.mapper.PersonMapper;
import com.example.tech.splitbiller.model.response.PersonResponse;
import com.example.tech.splitbiller.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final PersonMapper personMapper;

    public GroupMemberService(GroupMemberRepository groupMemberRepository,
                              PersonMapper personMapper) {
        this.groupMemberRepository = groupMemberRepository;
        this.personMapper = personMapper;
    }

    public List<PersonResponse> getAllPersonWithInGroup(Group group) {
        return groupMemberRepository.findByGroup(group).stream().map(groupMember -> personMapper.toPersonResponse(groupMember.getPerson())).collect(Collectors.toList());
    }

    public void validateMember(Group group, Person person) {
        Optional<GroupMember> byGroupAndPerson = groupMemberRepository.findByGroupAndPerson(group, person);
        if(byGroupAndPerson.isEmpty())
            throw new DataInvalidException("Person is not a member of group ");
    }
}
