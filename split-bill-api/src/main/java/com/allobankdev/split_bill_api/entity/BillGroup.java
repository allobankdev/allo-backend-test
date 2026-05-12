package com.allobankdev.split_bill_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bill_groups")
public class BillGroup extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();
}
