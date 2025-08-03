package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.entity.CalegEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalegRepository extends JpaRepository<CalegEntity, UUID> {

    List<CalegEntity> findByDapilIdAndPartaiId(UUID dapilId, UUID partaiId, Sort sort);

    List<CalegEntity> findByDapilId(UUID dapilId, Sort sort);

    List<CalegEntity> findByPartaiId(UUID partaiId, Sort sort);
}
