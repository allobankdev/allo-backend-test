package com.allobank.splitbill.api;

import com.allobank.splitbill.api.dto.request.CreateUserRequest;
import com.allobank.splitbill.api.dto.response.PersonalBillsResponse;
import com.allobank.splitbill.api.dto.response.UserResponse;
import com.allobank.splitbill.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = service.createUser(request);
        return ResponseEntity.created(URI.create("/api/users/" + response.username())).body(response);
    }

    @GetMapping("/{username}")
    public UserResponse getUser(@PathVariable String username) {
        return service.getUser(username);
    }

    @GetMapping("/{username}/bills")
    public PersonalBillsResponse getPersonalBills(@PathVariable String username) {
        return service.getPersonalBills(username);
    }
}
