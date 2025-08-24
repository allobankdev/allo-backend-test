package com.allobank.allobackendtest.specification;

import com.allobank.allobackendtest.entity.CalegEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CalegSpecification {

    // Filter by dapil.id
    public static Specification<CalegEntity> hasDapilId(UUID dapilId) {
        return (root, query, cb) -> dapilId == null ? null : cb.equal(root.get("dapil").get("id"), dapilId);
    }

    // Filter by partai.id
    public static Specification<CalegEntity> hasPartaiId(UUID partaiId) {
        return (root, query, cb) -> partaiId == null ? null : cb.equal(root.get("partai").get("id"), partaiId);
    }

    // Filter by nama_dapil (case-insensitive, partial match)
    public static Specification<CalegEntity> hasNamaDapil(String namaDapil) {
        return (root, query, cb) -> {
            if (namaDapil == null || namaDapil.trim().isEmpty()) return null;
            return cb.like(
                    cb.lower(root.get("dapil").get("namaDapil")),
                    "%" + namaDapil.toLowerCase().trim() + "%"
            );
        };
    }

    // Filter by nama_partai (case-insensitive, partial match)
    public static Specification<CalegEntity> hasNamaPartai(String namaPartai) {
        return (root, query, cb) -> {
            if (namaPartai == null || namaPartai.trim().isEmpty()) return null;
            return cb.like(
                    cb.lower(root.get("partai").get("namaPartai")),
                    "%" + namaPartai.toLowerCase().trim() + "%"
            );
        };
    }

    // Combine all specifications
    public static Specification<CalegEntity> searchCaleg(
            UUID dapilId,
            UUID partaiId,
            String namaDapil,
            String namaPartai) {

        return Specification
                .where(hasDapilId(dapilId))
                .and(hasPartaiId(partaiId))
                .and(hasNamaDapil(namaDapil))
                .and(hasNamaPartai(namaPartai));
    }
}