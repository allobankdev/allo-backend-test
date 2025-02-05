package com.allobank.allobackendtest;

import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.service.impl.CalegServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalegServiceImplTest {

    //Test
    @Mock
    private CalegRepository calegRepository;

    @InjectMocks
    private CalegServiceImpl calegServiceImpl;

    private CalegEntity calegEntity;
    private PartaiEntity partaiEntity;
    private DapilEntity dapilEntity;

    @BeforeEach
    void setUp() {
        UUID partaiId = UUID.randomUUID();
        UUID dapilId = UUID.randomUUID();
        UUID calegId = UUID.randomUUID();

        partaiEntity = new PartaiEntity();
        partaiEntity.setId(partaiId);
        partaiEntity.setNamaPartai("Partai A");
        partaiEntity.setNomorUrut(1);

        dapilEntity = new DapilEntity();
        dapilEntity.setId(dapilId);
        dapilEntity.setNamaDapil("Dapil A");
        dapilEntity.setProvinsi("Provinsi A");
        dapilEntity.setJumlahKursi(10);
        dapilEntity.setWilayahDapilEntityList(List.of());


        calegEntity = new CalegEntity();
        calegEntity.setId(calegId);
        calegEntity.setNama("Caleg 1");
        calegEntity.setJenisKelamin(JenisKelamin.valueOf("LAKILAKI"));
        calegEntity.setNomorUrut(1);
        calegEntity.setPartaiEntity(partaiEntity);
        calegEntity.setDapilEntity(dapilEntity);
    }

    @Test
    void listCalegs_ShouldReturnList_WhenNoFiltersAreProvided() {
        when(calegRepository.findAll(Mockito.any(Sort.class))).thenReturn(List.of(calegEntity));

        List<Caleg> result = calegServiceImpl.listCalegs(null, null, "asc");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNama()).isEqualTo("Caleg 1");
        assertThat(result.get(0).getPartai().getNamaPartai()).isEqualTo("Partai A");
        assertThat(result.get(0).getDapil().getNamaDapil()).isEqualTo("Dapil A");
    }

    @Test
    void listCalegs_ShouldFilterByDapil() {
        when(calegRepository.findByDapilEntity_NamaDapil(Mockito.eq("Dapil A"), Mockito.any(Sort.class)))
                .thenReturn(List.of(calegEntity));

        List<Caleg> result = calegServiceImpl.listCalegs("Dapil A", null, "asc");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDapil().getNamaDapil()).isEqualTo("Dapil A");
    }

    @Test
    void listCalegs_ShouldFilterByPartai() {
        when(calegRepository.findByPartaiEntity_NamaPartai(Mockito.eq("Partai A"), Mockito.any(Sort.class)))
                .thenReturn(List.of(calegEntity));

        List<Caleg> result = calegServiceImpl.listCalegs(null, "Partai A", "asc");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPartai().getNamaPartai()).isEqualTo("Partai A");
    }
}
