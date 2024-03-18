package com.example.demo.src.payment;

import com.example.demo.src.payment.model.PostPaymentRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/app/payments")
public class PaymentController {

    private final JwtService jwtService;
    private final PaymentsService paymentService;


    @PostMapping("/verify/{impUid}")
    public ResponseEntity<PostPaymentRes> verifyPayment(@PathVariable String impUid) {
        try {
            // 유저 ID는 JWT 토큰에서 추출합니다. 실제 구현에서는 토큰 검증 로직을 통해 ID를 추출해야 합니다.
            Long userId = jwtService.getUserId();

            // PaymentService의 결제 검증 메소드를 호출합니다.
            PostPaymentRes postPaymentRes = paymentService.verifyAndProcessPayment(impUid, userId);

            return ResponseEntity.ok(postPaymentRes);
        } catch (Exception e) {
            log.error("Payment verification failed", e);
            // 실패 응답 처리. 실제 애플리케이션에서는 더 구체적인 예외 처리 및 사용자에게 유용한 오류 메시지 제공이 필요합니다.
            return ResponseEntity.badRequest().build();
        }
    }
}

