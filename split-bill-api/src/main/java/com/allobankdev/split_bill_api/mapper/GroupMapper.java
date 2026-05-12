package com.allobankdev.split_bill_api.mapper;

import com.allobankdev.split_bill_api.dto.CreateGroupRequest;
import com.allobankdev.split_bill_api.entity.BillGroup;
import com.allobankdev.split_bill_api.entity.Participant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupMapper {

    public BillGroup toEntity(CreateGroupRequest request) {

        BillGroup group = new BillGroup();
        group.setName(request.getName());

        List<Participant> participants =
                request.getParticipants()
                        .stream()
                        .map(name -> {

                            Participant participant =
                                    new Participant();

                            participant.setName(name);
                            participant.setGroup(group);

                            return participant;

                        }).collect(Collectors.toList());

        group.setParticipants(participants);

        return group;
    }
}
