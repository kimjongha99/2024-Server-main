package com.example.demo.src.payment.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.enums.PaymentStatus;
import com.example.demo.src.user.entity.User;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "PAYMENT")
@Audited

public class Payments extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String impUid;

    @Column(nullable = false)
    private String merchantUid;

    @Column(nullable = false)
    private BigDecimal paidAmount;

    @Column(nullable = false)
    private LocalDateTime approvedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private String payMethod; // 결제 수단

    @Column(nullable = false)
    private String pgProvider; // 결제 게이트웨이 제공자

    @Column(nullable = false)
    private String buyerEmail; // 구매자 이메일

    @Column(nullable = false)
    private String buyerName; // 구매자 이름

    @Column(nullable = false)
    private String buyerTel; // 구매자 전화번호

    @Column(nullable = false)
    private String buyerAddr; // 구매자 주소

    @Column(nullable = false)
    private String buyerPostcode; // 구매자 우편번호
    @OneToOne(mappedBy = "payments", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Subscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
