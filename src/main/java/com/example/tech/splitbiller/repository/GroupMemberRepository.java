package com.example.tech.splitbiller.repository;

import com.example.tech.splitbiller.entity.Group;
import com.example.tech.splitbiller.entity.GroupMember;
import com.example.tech.splitbiller.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {
    Optional<GroupMember> findByGroupAndPerson(Group group, Person person);

    List<GroupMember> findByGroup(Group group);
}
