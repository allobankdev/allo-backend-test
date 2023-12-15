package com.allobank.allobackendtest.core.local.caleg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.allobank.allobackendtest.core.local.caleg.model.Caleg;

@SpringBootTest
public class CalegRepositoryTest {

    @Test
    void testFindByDapilAndPartai() {
        CalegRepository calegRepository = mock(CalegRepository.class);

        List<Caleg> calegs = new ArrayList<>();
        calegs.add(new Caleg());

        when(calegRepository.findByDapilAndPartai(anyString(), anyString())).thenReturn(calegs);

        List<Caleg> result = calegRepository.findByDapilAndPartai("dapil", "partai");

        assertEquals(1, result.size());
    }

    @Test
    void testFindOneByNama() {
        CalegRepository calegRepository = mock(CalegRepository.class);

        Caleg caleg = new Caleg();

        when(calegRepository.findOneByNama(anyString())).thenReturn(Optional.of(caleg));

        Optional<Caleg> result = calegRepository.findOneByNama("nama");

        assertEquals(caleg, result.orElse(null));
    }
}
