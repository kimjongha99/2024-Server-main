package com.example.demo.src.payment;


import com.example.demo.common.enums.PaymentStatus;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.payment.entity.Payments;
import com.example.demo.src.payment.model.PostPaymentRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Transactional
@RequiredArgsConstructor
@Service
public class PaymentsService {

    private final PaymentsRepository paymentRepository;
    private final UserRepository userRepository;
    private final IamportClient iamportClient;
    public PostPaymentRes verifyAndProcessPayment(String impUid, Long userId) throws IamportResponseException, IOException {
        IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(impUid);
        Payment payment = paymentResponse.getResponse();

        if (payment == null) {
            throw new BaseException(BaseResponseStatus.PAYMENT_VERIFICATION_FAILED);
        }
        if (payment.getStatus().equals("paid")) {
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));


            Payments newPayment = Payments.builder()
                    .impUid(payment.getImpUid())
                    .merchantUid(payment.getMerchantUid())
                    .paidAmount(payment.getAmount())
                    .approvedAt(LocalDateTime.now()) // 실제 결제 시각을 사용해야 할 수도 있습니다.
                    .paymentStatus(PaymentStatus.OK)
                    .payMethod(payment.getPayMethod())
                    .pgProvider(payment.getPgProvider())
                    .buyerEmail(payment.getBuyerEmail())
                    .buyerName(payment.getBuyerName())
                    .buyerTel(payment.getBuyerTel())
                    .buyerAddr(payment.getBuyerAddr())
                    .buyerPostcode(payment.getBuyerPostcode())
                    .user(user)
                    .build();
            paymentRepository.save(newPayment);

            return new PostPaymentRes(impUid, "결제가 성공적으로 처리되었습니다.");
        } else {
            throw new BaseException(BaseResponseStatus.PAYMENT_VERIFICATION_FAILED);
        }
    }

}