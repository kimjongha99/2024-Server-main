package com.example.demo.src.payment;

import com.example.demo.src.payment.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long>{


    boolean existsByImpUid(String impUid);

    List<Payments> findByPaidAmountNot(BigDecimal bigDecimal);
}
