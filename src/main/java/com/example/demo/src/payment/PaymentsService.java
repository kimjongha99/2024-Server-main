package com.example.demo.src.payment;


import com.example.demo.common.enums.PaymentStatus;
import com.example.demo.common.enums.SubscriptionStatus;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.payment.entity.Payments;
import com.example.demo.src.payment.entity.Subscription;
import com.example.demo.src.payment.model.PostPaymentRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class PaymentsService {

    private final PaymentsRepository paymentRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final IamportClient iamportClient;

    public PostPaymentRes verifyAndProcessPayment(String impUid, Long userId) throws IamportResponseException, IOException {
        // 결제 정보 조회
        IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(impUid);
        Payment payment = paymentResponse.getResponse();

        // 결제 정보가 없거나, 결제 상태가 'paid'가 아니거나, 결제 금액이 만원이 아닌 경우
        if (payment == null || !"paid".equals(payment.getStatus()) || payment.getAmount().compareTo(new BigDecimal("10000")) != 0) {
            // 결제 금액이 만원이 아닌 경우, 결제 취소 로직을 수행할 수도 있습니다.
            if (payment.getAmount().compareTo(new BigDecimal("10000")) != 0) {
                cancelPayment(impUid); // 금액 불일치로 결제 취소
            }
            throw new BaseException(BaseResponseStatus.PAYMENT_VERIFICATION_FAILED);
        }


        // 이미 존재하는 결제인지 확인
        if (paymentRepository.existsByImpUid(payment.getImpUid())) {
            throw new BaseException(BaseResponseStatus.ALREADY_EXIST_PAYMENT);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));

        // 구독 정보 확인 및 추가
        if (subscriptionRepository.existsByUserAndSubscriptionStatus(user, SubscriptionStatus.OK)) {
            cancelPayment(impUid); // 결제 취소 로직
            throw new BaseException(BaseResponseStatus.ALREADY_EXIST_SUBSCRIPTION);
        }

        // 새 결제 정보 생성 및 저장
        Payments newPayment = createAndSavePayment(payment, user);
        Subscription newSubscription = createSubscribe(user, newPayment);
        subscriptionRepository.save(newSubscription);

        return new PostPaymentRes(impUid, "결제 및 구독이 성공적으로 처리되었습니다.");
    }

    private void cancelPayment(String impUid) {
        try {
            // CancelData 객체를 생성하고, 필요한 경우 취소 사유를 설정합니다.
            // 여기서는 예시로 "중복 결제"라는 취소 사유를 사용합니다.
            CancelData cancelData = new CancelData(impUid, true); // 전체 취소를 의미
            cancelData.setReason("중복 결제"); // 취소 사유 설정

            // 아임포트 결제 취소 API 호출
            IamportResponse<Payment> paymentCancellationResponse = iamportClient.cancelPaymentByImpUid(cancelData);

            // 결제 취소 응답 처리
            if (paymentCancellationResponse.getResponse() != null) {
                // 취소 성공 로그, 처리 등
                System.out.println("결제 취소 성공: " + impUid);
            } else {
                // 취소 실패 로그, 처리 등
                System.err.println("결제 취소 실패: " + impUid);
            }
        } catch (IamportResponseException e) {
            // 아임포트 서버 응답 에러 처리
            System.err.println("IamportResponseException: " + e.getMessage());
        } catch (IOException e) {
            // 네트워크 IO 에러 처리
            System.err.println("IOException: " + e.getMessage());
        }
    }

    private Payments createAndSavePayment(Payment payment, User user) {
        Payments newPayment = Payments.builder()
                .impUid(payment.getImpUid())
                .merchantUid(payment.getMerchantUid())
                .paidAmount(payment.getAmount())
                .approvedAt(LocalDateTime.now())
                .paymentStatus(payment.getStatus().equals("paid") ? PaymentStatus.OK : PaymentStatus.CANCEL)
                .payMethod(payment.getPayMethod())
                .pgProvider(payment.getPgProvider())
                .buyerEmail(payment.getBuyerEmail())
                .buyerName(payment.getBuyerName())
                .buyerTel(payment.getBuyerTel())
                .buyerAddr(payment.getBuyerAddr())
                .buyerPostcode(payment.getBuyerPostcode())
                .user(user)
                .build();
        return paymentRepository.save(newPayment);
    }

    private Subscription createSubscribe(User user, Payments payment) {
        Subscription subscription = Subscription.builder()
                .user(user)
                .payments(payment)
                .subscribeAt(LocalDateTime.now())
                .subscriptionStatus(SubscriptionStatus.OK)
                .build();
        return subscription;
    }



    public void verifyPayments() {
        // paidAmount가 10,000.00과 일치하지 않는 모든 결제 레코드를 가져옵니다.
        List<Payments> discrepancyPayments = paymentRepository.findByPaidAmountNot(new BigDecimal("10000.00"));
        // 금액 불일치 레코드에 대해 로그를 남깁니다.
        for (Payments payment : discrepancyPayments) {
            System.out.println("금액 불일치 발견: " + payment.getImpUid() + ", 금액: " + payment.getPaidAmount());
        }

        // 불일치하는 결제가 없을 경우의 로그
        if (discrepancyPayments.isEmpty()) {
            System.out.println("모든 결제 금액이 정확합니다.");
        }
    }

    }

