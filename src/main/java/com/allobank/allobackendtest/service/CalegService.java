package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.entity.Caleg;
import com.allobank.allobackendtest.model.CalegResponse;
import com.allobank.allobackendtest.model.SearchCalegRequest;
import com.allobank.allobackendtest.repository.CalegRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class CalegService {

	@Autowired
  	private CalegRepository calegRepository;

	private CalegResponse toCalegResponse(Caleg caleg) {
		return CalegResponse.builder()
			.id(caleg.getId())
			.nama(caleg.getNama())
			.nomor_urut(caleg.getNomor_urut())
			.dapil_id(caleg.getDapil().getId())
			.nama_dapil(caleg.getDapil().getNamaDapil())
			.provinsi(caleg.getDapil().getProvinsi())
			.wilayah_dapil(caleg.getDapil().getWilayahDapilList())
			.partai_id(caleg.getPartai().getId())
			.nama_partai(caleg.getPartai().getNamaPartai())
			.jenisKelamin(caleg.getJenisKelamin())
			.build();
	}

	@Transactional(readOnly = true)
	public Page<CalegResponse> filter(SearchCalegRequest request){
		Specification<Caleg> specification = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (Objects.nonNull(request.getDapil_id())) {
				predicates.add(builder.like(root.get("dapil_id"), "%" + request.getDapil_id() + "%"));
			}
			if (Objects.nonNull(request.getPartai_id())) {
				predicates.add(builder.like(root.get("partai_id"), "%" + request.getPartai_id() + "%"));
			}
			if (Objects.nonNull(request.getNomor_urut())) {
				predicates.add(builder.like(root.get("nomor_urut"), "%" + request.getNomor_urut() + "%"));
			}
			if (Objects.nonNull(request.getJenisKelamin())) {
				predicates.add(builder.like(root.get("jenisKelamin"), "%" + request.getJenisKelamin() + "%"));
			}

			return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
		};

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
		Page<Caleg> calegs = calegRepository.findAll(specification, pageable);
		List<CalegResponse> calegResponses = calegs.stream()
			.map(this::toCalegResponse)
			.toList();

		return new PageImpl<>(calegResponses, pageable, calegs.getTotalElements());
	}
}
