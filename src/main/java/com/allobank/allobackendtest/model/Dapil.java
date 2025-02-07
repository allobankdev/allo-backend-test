package com.allobank.allobackendtest.model;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

// import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Data
public class Dapil {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank(message = "Nama dapil is required")
    @Column(unique = true)
    private String nama_dapil;
}
