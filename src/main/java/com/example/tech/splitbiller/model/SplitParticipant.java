package com.example.tech.splitbiller.model;

import com.example.tech.splitbiller.entity.Person;

import java.math.BigDecimal;

public record SplitParticipant (
        Person person,

        BigDecimal quantity,

        BigDecimal amount,

        BigDecimal percentage
) {}
