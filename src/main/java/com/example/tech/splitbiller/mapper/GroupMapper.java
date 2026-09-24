package com.example.tech.splitbiller.mapper;

import com.example.tech.splitbiller.entity.Group;
import com.example.tech.splitbiller.entity.GroupMember;
import com.example.tech.splitbiller.model.response.GroupResponse;
import com.example.tech.splitbiller.model.response.PersonResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMapper {

    private final PersonMapper personMapper;

    public GroupMapper(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    public GroupResponse toGroupResponse(Group group) {
        List<PersonResponse> members = group.getMemberships()
                .stream()
                .map(GroupMember::getPerson)
                .map(personMapper::toPersonResponse)
                .toList();

        return new GroupResponse(group.getId(), group.getName(), group.getDescription(), group.getCreatedAt(), members);
    }
}
