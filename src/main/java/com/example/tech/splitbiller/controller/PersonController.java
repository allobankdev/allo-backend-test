package com.example.tech.splitbiller.controller;

import com.example.tech.splitbiller.model.request.CreatePersonRequest;
import com.example.tech.splitbiller.model.response.BaseResponse;
import com.example.tech.splitbiller.model.response.CreatePersonResponse;
import com.example.tech.splitbiller.model.response.PersonResponse;
import com.example.tech.splitbiller.service.PersonService;
import com.example.tech.splitbiller.util.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.API_PATH + Constants.PERSON_PATH)
@Validated
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(value = Constants.CREATE_PATH)
    public ResponseEntity<BaseResponse<PersonResponse>> createPerson(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                                                     @Valid @RequestBody CreatePersonRequest request) {

        PersonResponse response = personService.createPerson(idempotencyKey, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.<PersonResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }
}
