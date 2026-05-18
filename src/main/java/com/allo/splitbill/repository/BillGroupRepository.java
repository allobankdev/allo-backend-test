package com.allo.splitbill.repository;

import com.allo.splitbill.model.BillGroup;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BillGroupRepository {

    private final Map<String, BillGroup> groups = new ConcurrentHashMap<>();

    public BillGroup save(BillGroup group) {
        groups.put(group.getId(), group);
        return group;
    }

    public Optional<BillGroup> findById(String id) {
        return Optional.ofNullable(groups.get(id));
    }
}
