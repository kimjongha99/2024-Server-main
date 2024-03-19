package com.example.demo.scheduler;

import com.example.demo.src.payment.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentVerificationScheduler {

    private final PaymentsService paymentsService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void verifyPayments() {
        // 결제 금액 검증 로직 실행
        paymentsService.verifyPayments();
    }
}
