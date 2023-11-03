package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dapil")
public class Dapil {

	@Id
	private String id;

	private String namaDapil;

	private String provinsi;

	private String wilayahDapilList;

	private String jumlahKursi;

	@OneToMany(mappedBy = "dapil", fetch = FetchType.EAGER)
	private List<Caleg> caleg;
}
