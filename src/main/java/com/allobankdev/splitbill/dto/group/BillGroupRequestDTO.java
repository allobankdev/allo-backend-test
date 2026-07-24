package com.allobankdev.splitbill.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillGroupRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotEmpty(message = "Participants cannot be empty")
    private List<String> participants;
}
