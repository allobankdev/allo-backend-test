package com.allobank.allobackendtest.entity;

import com.allobank.allobackendtest.model.JenisKelamin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CalegEntityTest {

    private CalegEntity caleg;

    @BeforeEach
    void setUp() {
        caleg = CalegEntity.builder()
                .id(UUID.randomUUID())
                .nomorUrut(1)
                .nama("John Doe")
                .jenisKelamin(JenisKelamin.LAKILAKI)
                .build();
    }

    @Test
    void testGetterSetter() {
        UUID newId = UUID.randomUUID();
        caleg.setId(newId);
        caleg.setNama("Jane Doe");
        caleg.setNomorUrut(2);
        caleg.setJenisKelamin(JenisKelamin.PEREMPUAN);

        assertThat(caleg.getId()).isEqualTo(newId);
        assertThat(caleg.getNama()).isEqualTo("Jane Doe");
        assertThat(caleg.getNomorUrut()).isEqualTo(2);
        assertThat(caleg.getJenisKelamin()).isEqualTo(JenisKelamin.PEREMPUAN);
    }

    @Test
    void testBuilder() {
        assertThat(caleg.getNama()).isEqualTo("John Doe");
        assertThat(caleg.getNomorUrut()).isEqualTo(1);
        assertThat(caleg.getJenisKelamin()).isEqualTo(JenisKelamin.LAKILAKI);
    }

    @Test
    void testPrePersistAndPreUpdate() {
        // Simulasi callback PrePersist
        caleg.onCreate();
        LocalDateTime createdAt = caleg.getCreatedAt();
        LocalDateTime updatedAt = caleg.getUpdatedAt();

        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();

        // Simulasi callback PreUpdate
        caleg.onUpdate();
        assertThat(caleg.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
    }

    @Test
    void testRelations() {
        DapilEntity dapil = new DapilEntity();
        PartaiEntity partai = new PartaiEntity();

        caleg.setDapil(dapil);
        caleg.setPartai(partai);

        assertThat(caleg.getDapil()).isEqualTo(dapil);
        assertThat(caleg.getPartai()).isEqualTo(partai);
    }
}
