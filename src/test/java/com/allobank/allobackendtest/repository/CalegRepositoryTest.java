package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CalegRepositoryTest {

    @Autowired
    private CalegRepository calegRepository;

    @Autowired
    private PartaiRepository partaiRepository;

    @Autowired
    private DapilRepository dapilRepository;

    @Test
    void testFindByDapilIdAndPartaiId() {
        try {
            PartaiEntity partai = new PartaiEntity();
            partai.setNamaPartai("Partai Uji");
            partai.setNomorUrut(10);
            partai = partaiRepository.save(partai);

            DapilEntity dapil = new DapilEntity();
            dapil.setNamaDapil("Dapil Test");
            dapil.setProvinsi("Jawa Barat");
            dapil.setJumlahKursi(5);
            dapil.setWilayahDapilList(Arrays.asList("Kota A", "Kota B"));
            dapil = dapilRepository.save(dapil);

            CalegEntity caleg = new CalegEntity();
            caleg.setNama("Agil Nurdiansah");
            caleg.setNomorUrut(1);
            caleg.setJenisKelamin(JenisKelaminEnum.LAKILAKI);
            caleg.setPartai(partai);
            caleg.setDapil(dapil);
            caleg = calegRepository.save(caleg);

            List<CalegEntity> result = calegRepository.findByDapilIdAndPartaiId(
                    dapil.getId(),
                    partai.getId(),
                    Sort.by("nomorUrut")
            );

            assertThat(result).isNotEmpty();
            assertThat(result.get(0).getNama()).isEqualTo("Agil Nurdiansah");

            System.out.println("✅ testFindByDapilIdAndPartaiId: Berhasil");
        } catch (AssertionError | Exception e) {
            System.err.println("❌ testFindByDapilIdAndPartaiId: Gagal - " + e.getMessage());
            throw e;
        }
    }
}
