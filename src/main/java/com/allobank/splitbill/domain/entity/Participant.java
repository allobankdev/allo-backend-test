package com.allobank.splitbill.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore
    private BillGroup group;
}
