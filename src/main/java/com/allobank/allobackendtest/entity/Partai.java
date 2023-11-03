package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "partai")
public class Partai {

	@Id
	private String id;

	private String namaPartai;

	private String nomorUrut;

	@OneToMany(mappedBy = "partai", fetch = FetchType.EAGER)
	private List<Caleg> caleg;
}
