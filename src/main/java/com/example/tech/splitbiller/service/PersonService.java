package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.entity.IdempotencyKey;
import com.example.tech.splitbiller.entity.Person;
import com.example.tech.splitbiller.exception.DataNotFoundException;
import com.example.tech.splitbiller.exception.TransactionConflictException;
import com.example.tech.splitbiller.mapper.PersonMapper;
import com.example.tech.splitbiller.model.request.CreatePersonRequest;
import com.example.tech.splitbiller.model.response.PersonResponse;
import com.example.tech.splitbiller.repository.PersonRepository;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final IdempotencyKeyService idempotencyKeyService;
    private final ObjectMapper objectMapper;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository,
                         IdempotencyKeyService idempotencyKeyService,
                         ObjectMapper objectMapper,
                         PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.idempotencyKeyService = idempotencyKeyService;
        this.objectMapper = objectMapper;
        this.personMapper = personMapper;
    }

    public List<Person> findAllById(List<String> ids) {
        return personRepository.findAllById(ids);
    }

    public Person getById(String id) {
        return personRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Person not found"));
    }

    public PersonResponse createPerson(String idempotencyKey, CreatePersonRequest request) {
        String hashedRequest = objectMapper.writeValueAsString(request);
        Optional<IdempotencyKey> find = idempotencyKeyService.findByIdempotencyKey(idempotencyKey);
        if(find.isPresent()) {
            IdempotencyKey idempotencyKeyObj = find.get();
            if(!idempotencyKeyObj.getHashRequest().equals(hashedRequest))
                throw new TransactionConflictException("Idempotency-Key was already used with a different request");

            String response = idempotencyKeyObj.getResponse();
            return objectMapper.readValue(response, PersonResponse.class);
        }

        long epochNow = Instant.now().toEpochMilli();
        Person person = new Person();
        person.setName(request.name());
        person.setEmail(request.email());
        person.setCreatedAt(epochNow);
        Person saved = personRepository.save(person);
        PersonResponse response = personMapper.toPersonResponse(saved);
        idempotencyKeyService.save(idempotencyKey, hashedRequest, objectMapper.writeValueAsString(response));
        return response;
    }
}
