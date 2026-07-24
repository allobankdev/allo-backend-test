package com.allobankdev.splitbill.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bill_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "group_participants", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "participant_name")
    @Builder.Default
    private List<String> participants = new ArrayList<>();
}
