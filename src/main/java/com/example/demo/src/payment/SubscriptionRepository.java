package com.example.demo.src.payment;

import com.example.demo.common.enums.SubscriptionStatus;
import com.example.demo.src.payment.entity.Subscription;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {


    boolean existsByUserAndSubscriptionStatus(User user, SubscriptionStatus status);

    Optional<Subscription> findByUser(User user);
}
