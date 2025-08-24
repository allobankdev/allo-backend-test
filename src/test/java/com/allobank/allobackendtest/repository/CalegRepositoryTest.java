package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.model.JenisKelamin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CalegRepositoryTest {

    @Autowired
    private CalegRepository calegRepository;

    @Autowired
    private DapilRepository dapilRepository;

    @Autowired
    private PartaiRepository partaiRepository;

    private DapilEntity dapil;
    private PartaiEntity partai;


    @BeforeEach
    void setUp() {
        dapil = dapilRepository.save(DapilEntity.builder()
                .namaDapil("Dapil 1")
                .provinsi("Jawa Barat") // tambahkan ini
                .jumlahKursi(5)         // jika kolom ini NOT NULL juga
                .build());

        partai = partaiRepository.save(PartaiEntity.builder()
                .namaPartai("Partai A")
                .nomorUrut(1)
                .build());
    }


    @Test
    void testSaveAndFindByNama() {
        CalegEntity caleg = CalegEntity.builder()
                .dapil(dapil)
                .partai(partai)
                .nomorUrut(1)
                .nama("Jane Doe")
                .jenisKelamin(JenisKelamin.PEREMPUAN)
                .build();

        calegRepository.save(caleg);

        List<CalegEntity> found = calegRepository.findByNama("Jane Doe");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getNama()).isEqualTo("Jane Doe");
    }

    @Test
    void testExistsByDapilIdAndNomorUrut() {
        CalegEntity caleg = CalegEntity.builder()
                .dapil(dapil)
                .partai(partai)
                .nomorUrut(2)
                .nama("John Doe")
                .jenisKelamin(JenisKelamin.LAKILAKI)
                .build();

        calegRepository.save(caleg);

        boolean exists = calegRepository.existsByDapilIdAndNomorUrut(dapil.getId(), 2);
        assertThat(exists).isTrue();

        boolean notExists = calegRepository.existsByDapilIdAndNomorUrut(dapil.getId(), 3);
        assertThat(notExists).isFalse();
    }
}
