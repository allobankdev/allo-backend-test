package com.allobank.allobackendtest.init;

import com.allobank.allobackendtest.entity.*;
import com.allobank.allobackendtest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PartaiRepository partaiRepository;
    private final DapilRepository dapilRepository;
    private final CalegRepository calegRepository;

    @Override
    public void run(String... args) throws Exception {
        // PARTAI
        PartaiEntity partai1 = new PartaiEntity();
        partai1.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        partai1.setNamaPartai("Partai Keadilan Bangsa");
        partai1.setNomorUrut(1);
        partaiRepository.save(partai1);

        PartaiEntity partai2 = new PartaiEntity();
        partai2.setId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
        partai2.setNamaPartai("Partai Rakyat Merdeka");
        partai2.setNomorUrut(2);
        partaiRepository.save(partai2);

        // DAPIL
        DapilEntity dapil1 = new DapilEntity();
        dapil1.setId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        dapil1.setNamaDapil("Dapil Jawa Barat I");
        dapil1.setProvinsi("Jawa Barat");
        dapil1.setWilayahDapilList(Arrays.asList("Kota Bandung", "Kota Cimahi"));
        dapil1.setJumlahKursi(10);
        dapilRepository.save(dapil1);

        DapilEntity dapil2 = new DapilEntity();
        dapil2.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
        dapil2.setNamaDapil("Dapil Jawa Tengah II");
        dapil2.setProvinsi("Jawa Tengah");
        dapil2.setWilayahDapilList(Arrays.asList("Kabupaten Semarang", "Kota Salatiga"));
        dapil2.setJumlahKursi(8);
        dapilRepository.save(dapil2);

        // CALEG
        CalegEntity caleg1 = new CalegEntity();
        caleg1.setId(UUID.fromString("c1c1c1c1-c1c1-c1c1-c1c1-c1c1c1c1c1c1"));
        caleg1.setNama("Agus Prasetyo");
        caleg1.setNomorUrut(1);
        caleg1.setJenisKelamin(JenisKelaminEnum.LAKILAKI);
        caleg1.setPartai(partai1);
        caleg1.setDapil(dapil1);
        calegRepository.save(caleg1);

    }
}
