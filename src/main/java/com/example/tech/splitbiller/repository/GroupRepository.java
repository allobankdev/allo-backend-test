package com.example.tech.splitbiller.repository;

import com.example.tech.splitbiller.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
    Optional<Group> findByIdAndDeleted(String groupId, Boolean deleted);
}
