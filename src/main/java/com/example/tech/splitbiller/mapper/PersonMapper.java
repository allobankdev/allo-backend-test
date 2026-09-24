package com.example.tech.splitbiller.mapper;

import com.example.tech.splitbiller.entity.Person;
import com.example.tech.splitbiller.model.response.PersonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    public PersonResponse toPersonResponse(Person person) {
        return new PersonResponse(person.getId(), person.getName(), person.getEmail(), person.getCreatedAt());
    }
}
