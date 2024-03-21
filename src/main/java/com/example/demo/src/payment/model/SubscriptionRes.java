package com.example.demo.src.payment.model;

import com.example.demo.common.enums.SubscriptionStatus;
import com.example.demo.src.payment.entity.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRes {

    private Long subscriptionId;
    private String subscriptionUserName;
    private BigDecimal paidAmount;
    private SubscriptionStatus subscriptionStatus;

    private  String datetimePay;

    public SubscriptionRes(Subscription subscription) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.subscriptionId = subscription.getId();
        this.subscriptionUserName = subscription.getUser().getName();
        this.paidAmount= subscription.getPayments().getPaidAmount();
        this.subscriptionStatus  = subscription.getSubscriptionStatus();
        this.datetimePay = subscription.getCreatedAt().format(formatter);

    }
}
