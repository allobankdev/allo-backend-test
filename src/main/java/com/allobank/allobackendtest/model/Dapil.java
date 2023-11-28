package com.allobank.allobackendtest.model;

import com.allobank.allobackendtest.model.converter.StringListToJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "dapil")
public class Dapil {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nama", updatable = true, nullable = false)
    private String namaDapil;

    @Column(name = "provinsi", updatable = true, nullable = false)
    private String provinsi;

    @Column(name = "wilayah_dapil", updatable = true, nullable = false)
    @Convert(converter = StringListToJsonConverter.class)
    @ColumnTransformer(write = "?::json")
    private List<String> wilayahDapilList;

    @Column(name = "jumlah_kursi", updatable = true, nullable = false)
    private Integer jumlahKursi;

    @Column(name = "created_at", updatable = true, nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", updatable = true, nullable = true)
    private Timestamp updatedAt;

    @Column(name = "deleted_at", updatable = true, nullable = true)
    private Timestamp deletedAt;
}
