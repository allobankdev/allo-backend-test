package com.allobank.splitbill.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private List<ParticipantResponse> participants;
}
