package com.example.demo.src.payment;

import com.example.demo.src.payment.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long>{


}
