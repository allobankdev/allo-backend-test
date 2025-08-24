package com.allobank.allobackendtest.mapper;

import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CalegMapperTest {

    @Autowired
    private CalegMapper calegMapper;

    @Test
    void testToEntity() {
        CalegDto dto = CalegDto.builder()
                .dapil(UUID.randomUUID())
                .partai(UUID.randomUUID())
                .nomorUrut(1)
                .nama("Jane Doe")
                .jenisKelamin(JenisKelamin.PEREMPUAN)
                .build();

        CalegEntity entity = calegMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getNama()).isEqualTo(dto.getNama());
        assertThat(entity.getNomorUrut()).isEqualTo(dto.getNomorUrut());
    }

    @Test
    void testToResponse() {
        DapilEntity dapil = new DapilEntity();
        PartaiEntity partai = new PartaiEntity();

        CalegEntity entity = CalegEntity.builder()
                .id(UUID.randomUUID())
                .dapil(dapil)
                .partai(partai)
                .nomorUrut(1)
                .nama("Jane Doe")
                .jenisKelamin(JenisKelamin.PEREMPUAN)
                .build();

        Caleg response = calegMapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.getNama()).isEqualTo(entity.getNama());
        assertThat(response.getNomorUrut()).isEqualTo(entity.getNomorUrut());
        assertThat(response.getJenisKelamin()).isEqualTo(entity.getJenisKelamin());
    }

    @Test
    void testUpdateFromRequest() {
        CalegDto dto = CalegDto.builder()
                .nama("Updated Name")
                .nomorUrut(2)
                .build();

        CalegEntity entity = CalegEntity.builder()
                .nama("Old Name")
                .nomorUrut(1)
                .build();

        calegMapper.updateFromRequest(dto, entity);

        assertThat(entity.getNama()).isEqualTo("Updated Name");
        assertThat(entity.getNomorUrut()).isEqualTo(2);
    }
}
