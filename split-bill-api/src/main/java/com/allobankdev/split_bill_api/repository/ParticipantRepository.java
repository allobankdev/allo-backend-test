package com.allobankdev.split_bill_api.repository;

import com.allobankdev.split_bill_api.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository
        extends JpaRepository<Participant, Long> {
}
