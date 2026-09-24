package com.example.tech.splitbiller.repository;

import com.example.tech.splitbiller.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByGroupIdOrderByPaidAtDesc(String groupId);

}
