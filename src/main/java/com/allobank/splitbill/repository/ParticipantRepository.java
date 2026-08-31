package com.allobank.splitbill.repository;

import com.allobank.splitbill.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByGroupId(Long groupId);
    Optional<Participant> findByIdAndGroupId(Long id, Long groupId);
}
