package com.allobankdev.split_bill_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateGroupRequest {

    @NotBlank
    private String name;

    @NotEmpty
    private List<String> participants;
}
