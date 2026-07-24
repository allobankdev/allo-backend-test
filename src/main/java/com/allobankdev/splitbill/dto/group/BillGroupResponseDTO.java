package com.allobankdev.splitbill.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillGroupResponseDTO {
    private String id;
    private String name;
    private List<String> participants;
}
