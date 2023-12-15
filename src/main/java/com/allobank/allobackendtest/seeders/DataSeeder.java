package com.allobank.allobackendtest.seeders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.allobank.allobackendtest.core.local.caleg.CalegRepository;
import com.allobank.allobackendtest.core.local.caleg.model.Caleg;
import com.allobank.allobackendtest.core.local.caleg.model.JenisKelamin;
import com.allobank.allobackendtest.core.local.dapil.DapilRepository;
import com.allobank.allobackendtest.core.local.dapil.model.Dapil;
import com.allobank.allobackendtest.core.local.partai.PartaiRepository;
import com.allobank.allobackendtest.core.local.partai.model.Partai;

@Component
public class DataSeeder {
    private final DapilRepository dapilRepository;
    private final PartaiRepository partaiRepository;
    private final CalegRepository calegRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataSeeder(DapilRepository dapilRepository, PartaiRepository partaiRepository,
            CalegRepository calegRepository, JdbcTemplate jdbcTemplate) {
        this.dapilRepository = dapilRepository;
        this.calegRepository = calegRepository;
        this.partaiRepository = partaiRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedDapil();
        seedPartai();
        seedCaleg();
    }

    private void seedDapil() {
        String dc0 = "Jawa Tengah", dc1 = "Jawa Barat", dc2 = "Jawa Timur";
        String sql = "SELECT nama_dapil FROM dapil c WHERE c.nama_dapil IN (\"" + dc0 + "\", \"" + dc1 + "\", \"" + dc2
                + "\")";
        List<Dapil> rs = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if (rs == null || rs.size() <= 0) {
            Dapil dapil = new Dapil("Jawa Timur", "Jawa", 100);
            Dapil dapil2 = new Dapil("Jawa Barat", "Jawa", 100);
            Dapil dapil3 = new Dapil("Jawa Tengah", "Jawa", 100);
            dapilRepository.save(dapil);
            dapilRepository.save(dapil2);
            dapilRepository.save(dapil3);
            System.out.println("seeder dapil berhasil.");
        } else {
            System.out.println("data dapil sudah ada.");
        }
    }

    private void seedPartai() {
        String dc0 = "PDI", dc1 = "GERINDRA", dc2 = "NASDEM";
        String sql = "SELECT nama_partai FROM partai p WHERE p.nama_partai IN (\"" + dc0 + "\", \"" + dc1 + "\", \""
                + dc2 + "\")";
        List<Partai> rsPartai = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if (rsPartai == null || rsPartai.size() <= 0) {
            Partai partai = new Partai("PDI", 01);
            Partai partai2 = new Partai("GERINDRA", 02);
            Partai partai3 = new Partai("NASDEM", 03);
            partaiRepository.save(partai);
            partaiRepository.save(partai2);
            partaiRepository.save(partai3);
            System.out.println("seeder partai berhasil.");
        } else {
            System.out.println("data partai sudah ada.");
        }
    }

    private void seedCaleg() {
        String dc0 = "Ganjar Pranowo", dc1 = "Prabowo Subianto", dc2 = "Anies Baswedan";
        String sql = "SELECT nama FROM caleg c WHERE c.nama IN (\"" + dc0 + "\", \"" + dc1 + "\", \"" + dc2 + "\")";
        List<Caleg> rsCaleg = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if (rsCaleg == null || rsCaleg.size() <= 0) {
            Dapil dapil = dapilRepository.findOneByNamaDapil("Jawa Tengah");
            List<Partai> partais = partaiRepository.findAll();
            for (Partai partai : partais) {
                if (partai.getNamaPartai().equals("PDI")) {
                    Caleg calegPdi = new Caleg(dapil, partai, 01, "Ganjar Pranowo", JenisKelamin.LAKILAKI);
                    calegRepository.save(calegPdi);
                }

                if (partai.getNamaPartai().equals("GERINDRA")) {
                    Caleg calegGrindra = new Caleg(dapil, partai, 02, "Prabowo Subianto",
                            JenisKelamin.LAKILAKI);
                    calegRepository.save(calegGrindra);
                }

                if (partai.getNamaPartai().equals("NASDEM")) {
                    Caleg calegNasdem = new Caleg(dapil, partai, 03, "Anies Baswedan",
                            JenisKelamin.LAKILAKI);
                    calegRepository.save(calegNasdem);
                }
            }

            System.out.println("seeder caleg berhasil.");
        } else {
            System.out.println("data caleg ini sudah ada.");
        }
    }
}
