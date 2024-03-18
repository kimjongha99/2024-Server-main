package com.example.demo.src.payment.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostPaymentRes {
    private String impUid; // 결제 고유 ID
    private String message; // 결제 검증 결과 메시지
    // 필요에 따라 추가 정보 포함 가능
}