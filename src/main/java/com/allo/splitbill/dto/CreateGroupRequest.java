package com.allo.splitbill.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateGroupRequest {

    @NotBlank(message = "Group name is required")
    private String name;

    @NotEmpty(message = "At least one participant is required")
    @Size(min = 2, message = "At least two participants are required")
    private List<@NotBlank String> participants;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
