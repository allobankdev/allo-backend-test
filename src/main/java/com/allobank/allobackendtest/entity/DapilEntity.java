package com.allobank.allobackendtest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dapil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DapilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, name = "nama_dapil")
    private String namaDapil;

    @Column(nullable = false)
    private String provinsi;

    @ElementCollection
    @CollectionTable(name = "dapil_wilayah", joinColumns = @JoinColumn(name = "dapil_id"))
    @Column(name = "wilayah")
    @Builder.Default
    private List<String> wilayahDapilList = new ArrayList<>();

    @Column(nullable = false)
    private int jumlahKursi;

    @OneToMany(mappedBy = "dapil", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("nomorUrut")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<CalegEntity> calegs = new ArrayList<>();



    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}